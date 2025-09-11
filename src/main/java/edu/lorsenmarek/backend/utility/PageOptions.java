package edu.lorsenmarek.backend.utility;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
public class PageOptions {
    private Integer baseId;
    private Integer pageSize;
    PageOptions() {
        baseId = null;
        pageSize = null;
    }
    PageOptions(Integer baseId, Integer pageSize) {
        this.baseId = baseId;
        this.pageSize = pageSize;
    }
}
