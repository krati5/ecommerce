package org.example.productservice.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class SortParam {
    private String paramName;
    // sortType can also by enum SortType
    private SortType sortType;
}
