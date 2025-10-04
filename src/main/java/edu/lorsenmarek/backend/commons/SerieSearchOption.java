package edu.lorsenmarek.backend.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class SerieSearchOption {
    private String title;
}
