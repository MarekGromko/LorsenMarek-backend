package edu.lorsenmarek.backend.common.option;

import lombok.*;

/**
 * Options pass to search a serie
 * @author Lorsen Lamour
 */
@Builder
@Data
@AllArgsConstructor
public class SerieSearchOptions {
    /** Create a new {@link SerieSearchOptions} */
    public SerieSearchOptions() {}
    private String title;
}
