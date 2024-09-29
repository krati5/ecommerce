package org.example.productservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.productservice.models.SortParam;

import java.util.List;

@Getter
@Setter
public class GetProductsRequestDto {
    private String query;
    private int numOfResults;
    private int offset;
    private List<SortParam> sortParamsList;
}
