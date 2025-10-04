package edu.lorsenmarek.backend.commons;

import lombok.*;


@AllArgsConstructor
@Data
@Builder
public class PageOptions {
    private Integer pageSize;
    private Integer pageIndex;
}
