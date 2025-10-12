package edu.lorsenmarek.backend.common;

/**
 * Common object that hold data that describe a mean
 * @param sum The sum of all the values
 * @param count The number of value in the sum
 */
public record MeanValue(double sum, double count) {
    /**
     * Compute the mean
     * return 0 if the mean is empty (has 0 count0
     * @return the mean value (sum/count)
     */
    public double getValue() {
        if (count == 0) return 0;
        return sum / count;
    }

    /**
     * If the count is equal to 0, the mean object is empty
     * @return true if the mean is empty, otherwise false
     */
    public boolean isEmpty() {
        return count == 0.0;
    }
}
