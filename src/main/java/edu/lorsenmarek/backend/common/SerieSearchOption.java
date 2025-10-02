package edu.lorsenmarek.backend.common;

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
