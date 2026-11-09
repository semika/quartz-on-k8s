package com.semika.learn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ProductResponseDto implements Serializable {

    @JsonProperty("total_num_records")
    private int totalNumberOfRecords;

    @JsonProperty("page_number")
    private int pageNumber;

    @JsonIgnore
    private List<ProductDto> products;
}
