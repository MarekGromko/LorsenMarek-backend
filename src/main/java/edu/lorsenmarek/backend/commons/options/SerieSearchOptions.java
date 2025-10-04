package edu.lorsenmarek.backend.commons.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class SerieSearchOptions {
    private String title;
}
