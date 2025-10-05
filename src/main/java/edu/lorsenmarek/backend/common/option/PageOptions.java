package edu.lorsenmarek.backend.common.option;

import lombok.*;


@AllArgsConstructor
@Data
@Builder
public class PageOptions {
    private Integer pageSize;
    private Integer pageIndex;
}
