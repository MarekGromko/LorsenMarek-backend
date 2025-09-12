package edu.lorsenmarek.backend.utility;

import lombok.*;


@AllArgsConstructor
@Data
@Builder
public class PageOptions {
    private Integer pageSize;
    private Integer pageIndex;
}
