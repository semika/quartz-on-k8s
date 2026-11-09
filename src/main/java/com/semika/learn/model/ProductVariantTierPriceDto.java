package com.semika.learn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductVariantTierPriceDto implements Serializable {

    @JsonProperty("tier_min")
    private String tierMin;

    @JsonProperty("tier_max")
    private String tierMax;

    @JsonProperty("price_per_unit")
    private String pricePerUnit;

}
