package edu.lorsenmarek.backend.common.option;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class SerieSearchOptions {
    private String title;
}
