package com.semika.learn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("company_id")
    private String companyId;

    @JsonProperty("store_id")
    private String storeId;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("media")
    private List<MediaDto> mediaDtoList;

    @JsonProperty("tags")
    private List<TagDto> tagDtoList;

    @JsonProperty("attributes")
    private List<AttributeDto> attributeDtoList;

    @JsonProperty("product_variants")
    private List<ProductVariantDto> productVariantDtoList;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("has_price_tiers")
    private Boolean hasTierPrice;

    @JsonProperty("has_enrich")
    private Boolean hasEnriched;

    @JsonProperty("additional_product_detail")
    private AdditionalDetailForProductDto additionalDetailForProductDto;

    @JsonProperty("store_slug")
    private String storeSlug;

    @JsonProperty("category_slug")
    private String categorySlug;

    @JsonProperty("product_slug")
    private String productSlug;

    @JsonProperty("pos_product_id")
    private String posProductId;

    @JsonProperty("discount_percentage")
    private String discountPercentage;

    @JsonProperty("overview_media")
    private String overviewMedia;

}
