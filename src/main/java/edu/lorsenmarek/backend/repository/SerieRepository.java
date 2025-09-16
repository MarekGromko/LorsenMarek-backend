package edu.lorsenmarek.backend.repository;
import edu.lorsenmarek.backend.model.Serie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class SerieRepository {

    private final JdbcTemplate jdbcTemplate;

    public SerieRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Serie> serieRowMapper = new RowMapper<>() {
        @Override
        public Serie mapRow(ResultSet rs, int rowNum) throws SQLException {
            Serie serie = new Serie();
            serie.setId(rs.getInt("id"));
            serie.setTitle(rs.getString("title"));
            serie.setGenre(rs.getString("genre"));
            serie.setNb_episode(rs.getInt("nb_episode"));
            serie.setNote(rs.getInt("note"));
            return serie;
        }
    };
    public List<Serie> searchByTitle(String title){
        String sql = "SELECT * FROM serie WHERE LOWER(title) LIKE ?";
        String pattern = "%" + title.toLowerCase() + "%";
        return jdbcTemplate.query(sql, serieRowMapper, pattern);
    }

    public List<Serie> findAll() {
        String sql = "SELECT * FROM serie";
        return jdbcTemplate.query(sql, serieRowMapper);
    }

    public Optional<Serie> findById(Integer id) {
        String sql = "SELECT * FROM serie WHERE id = ?";
        List<Serie> results = jdbcTemplate.query(sql, serieRowMapper, id);
        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.get(0));
        }
    }
    public List<Serie> search(String genre) {
        if (genre == null || genre.isEmpty()) {
            String sql = "SELECT * FROM serie";
            return jdbcTemplate.query(sql, serieRowMapper);
        } else {
            String sql = "SELECT * FROM serie WHERE genre = ?";
            return jdbcTemplate.query(sql, serieRowMapper, genre);
        }
    }

    public int save(Serie serie) {
        if (serie.getId() == null) {

            String sql = "INSERT INTO serie (title, genre, nb_episode, note) VALUES (?, ?, ?, ?)";
            return jdbcTemplate.update(sql, serie.getTitle(), serie.getGenre(), serie.getNb_episode(), serie.getNote());
        } else {

            String sql = "UPDATE serie SET title = ?, genre = ?, nb_episode = ?, note = ? WHERE id = ?";
            return jdbcTemplate.update(sql, serie.getTitle(), serie.getGenre(), serie.getNb_episode(), serie.getNote(), serie.getId());
        }
    }

    public int deleteById(Integer id) {
        String sql = "DELETE FROM serie WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    public boolean existsById(int id){
        String sql = "SELECT COUNT(*) FROM serie WHERE ID= ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,id);
        return count != null && count > 0;
    }



}