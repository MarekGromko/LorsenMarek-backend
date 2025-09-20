package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.model.*;
import edu.lorsenmarek.backend.repository.PersonSerieHistoryRepository;
import edu.lorsenmarek.backend.repository.SerieRepository;
import org.ejml.data.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.ImmutablePair;

import static org.ejml.dense.row.CommonOps_DDRM.*;

@Service
public class SerieRecommendationService {
    @Value("${service.recommendation.compute-interval:PT1M}")
    public String COMPUTE_INTERVAL;
    @Value("${service.recommendation.latent-node-heat:0.1}")
    public double LATENT_INIT_HEAT;
    @Value("${service.recommendation.batch-iteration:10}")
    public int BATCH_ITERATION;
    @Value("${service.recommendation.iteration:300}")
    public int ITERATION;
    @Value("${service.recommendation.learning-rate:0.004}")
    public double LEARNING_RATE;
    private final JdbcTemplate jdbc;
    private final SerieRepository serieRepo;
    private Instant last_computation;
    private DMatrix1Row transitiveMatrix;
    private List<Integer> peopleSortedIds;
    private List<Integer> seriesSortedIds;
    public SerieRecommendationService(final JdbcTemplate jdbc, final SerieRepository serieRepo) {
        this.jdbc = jdbc;
        this.serieRepo = serieRepo;
        last_computation = Instant.ofEpochSecond(0);
    }
    private double computeHistoryValue(PersonSerieHistory h) {
        double value = 0.0;
        value += 3*Math.exp(-Math.pow(
                (double)Duration.between(Instant.now(), h.getLastWatch()).toDays()/60,
                2.0
        )); // give some point for how recent a movie was watched
        value += 3*Math.exp(-Math.pow(
                (double) h.getInstanceWatch() / 2,
                2.0
        )); // give some point for the amount of time someone come back to watch the serie again;
        return value;
    }

    /**
     * Collaborative filtering using matrix factorization with latent nodes.
     * The parameters predicting the recommendation are not tweaked by humans,
     * they are generated using a gradient descent matrix factorization on runtime (lazily).
     * Latent node are gradually adjusted to optimize a loss function.
     * In this case, the loss function target a value assigned to each person & serie,
     * The value is based on the number of time the user came back to watch the serie,
     * and the last time the serie was watched.
     */
    private void computeTransitiveMatrix() {
        last_computation = Instant.now();
        peopleSortedIds = jdbc.queryForList("SELECT id FROM person ORDER BY id", Integer.class);
        seriesSortedIds = jdbc.queryForList("SELECT id FROM serie ORDER BY id", Integer.class);

        /// define the size of the different matrix
        final int NB_ROWS = peopleSortedIds.size();
        final int NB_COLS = seriesSortedIds.size();
        final int NB_LATENT_NODE = (int) Math.floor(Math.log(Math.min(peopleSortedIds.size(), seriesSortedIds.size()))/Math.log(2));
        //
        var targetMatrix        = new DMatrixRMaj(NB_ROWS, NB_COLS);
        var maskMatrix          = new DMatrixRMaj(NB_ROWS, NB_COLS);
        //
        // fill the targetMatrix & its mask
        for(var history : jdbc.query("SELECT * FROM person_serie_history ORDER BY person_id", new PersonSerieHistoryRepository.PersonSerieHistoryMapper())) {
            int rowIndex = Collections.binarySearch(peopleSortedIds, history.getPersonId());
            int colIndex = Collections.binarySearch(seriesSortedIds, history.getSerieId());
            ///
            targetMatrix.set(rowIndex, colIndex, computeHistoryValue(history));
            maskMatrix.set(rowIndex, colIndex, 1.0);
        }
        //targetMatrix.print("%5.2f");

        /// fill the latent nodes with random noise to start
        var peopleLatentNode = new DMatrixRMaj(NB_ROWS, NB_LATENT_NODE);
        var seriesLatentNode = new DMatrixRMaj(NB_LATENT_NODE, NB_COLS);
        for(int i = 0; i<peopleLatentNode.data.length; i++)
            peopleLatentNode.data[i] = RandomGenerator.getDefault().nextDouble()*LATENT_INIT_HEAT;
        for(int i = 0; i<seriesLatentNode.data.length; i++)
            seriesLatentNode.data[i] = RandomGenerator.getDefault().nextDouble()*LATENT_INIT_HEAT;

        transitiveMatrix = new DMatrixRMaj(NB_ROWS, NB_COLS);
        /// temporary matrix used for computation
        var costMatrix   = new DMatrixRMaj(NB_ROWS, NB_COLS);
        var peopleDelta  = new DMatrixRMaj(peopleLatentNode.numRows, peopleLatentNode.numCols);
        var seriesDelta  = new DMatrixRMaj(seriesLatentNode.numRows, seriesLatentNode.numCols);
        var transPeople = new DMatrixRMaj(peopleDelta.numCols, peopleDelta.numRows);
        var transSeries = new DMatrixRMaj(seriesDelta.numCols, seriesDelta.numRows);
        /// ITERATION ///
        for(int i = ITERATION; i-- > 0;) {
            mult(peopleLatentNode, seriesLatentNode, transitiveMatrix);
            subtract(transitiveMatrix, targetMatrix, costMatrix);
            elementMult(costMatrix, maskMatrix, costMatrix);
            for (int j = BATCH_ITERATION; j-- > 0; ) {
                ///
                transpose(peopleLatentNode, transPeople);
                transpose(seriesLatentNode, transSeries);

                mult(transPeople, costMatrix, seriesDelta);
                scale(LEARNING_RATE, seriesDelta);
                ///
                mult(costMatrix, transSeries, peopleDelta);
                scale(LEARNING_RATE, peopleDelta);
                ///
                subtract(peopleLatentNode, peopleDelta, peopleLatentNode);
                subtract(seriesLatentNode, seriesDelta, seriesLatentNode);
            }
        }
        //transitiveMatrix.print("%5.2f");
    }
    public List<ImmutablePair<Serie, Double>> computeRecommendation(int personId) {
        if(
                Duration.between(
                        last_computation,
                        Instant.now()).minus(Duration.parse(COMPUTE_INTERVAL).abs()
                ).isPositive()
        ) {
            transitiveMatrix = null;
        }
        if(transitiveMatrix == null ) computeTransitiveMatrix(); // avoid recalculating every time its called
        ////
        int rowIndex = Collections.binarySearch(peopleSortedIds, personId);
        return IntStream.range(0, transitiveMatrix.numCols)
                .mapToObj(i -> ImmutablePair.of(transitiveMatrix.get(rowIndex, i), i))
                .sorted(Comparator.reverseOrder())
                .limit(5)
                .map(pair -> ImmutablePair.of(serieRepo.findById(seriesSortedIds.get(pair.getValue())).orElse(null), pair.getKey()))
                .toList();
    }
}
