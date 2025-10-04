package edu.lorsenmarek.backend.commons.options;

import lombok.*;


@AllArgsConstructor
@Data
@Builder
public class PageOptions {
    private Integer pageSize;
    private Integer pageIndex;
}
