package edu.lorsenmarek.backend.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class SerieSearchOption {
    private String genre;
    private String title;
    private Integer minEpisode;

    public SerieSearchOption() {

    }
}
