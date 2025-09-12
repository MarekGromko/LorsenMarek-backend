package edu.lorsenmarek.backend.utility;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
public class PageOptions {
    private Integer pageSize;
    private Integer pageIndex;
    PageOptions(Integer pageSize, Integer pageIndex) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }
}
