package com.semika.learn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVariantDto implements Serializable {

    @JsonProperty("store_id")
    private String storeId;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_variant_id")
    private String productVariantId;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("upc")
    private String upc;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("label")
    private String label;

    @JsonProperty("stock")
    private long stock;

    @JsonProperty("max_amount")
    private int maxAmount;

    @JsonProperty("price")
    private double price;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("stock_status")
    private String stockStatus;

    @JsonProperty("has_stock")
    private Boolean hasStock;

    @JsonProperty("product_variant_tier_price_table")
    private List<ProductVariantTierPriceDto> tierPriceList;

    @JsonProperty("has_price_tiers")
    private Boolean hasTierPrice;

    @JsonProperty("uom")
    private String uom;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("external_product_id")
    private String externalProductId;

    @JsonProperty("display_order")
    private int displayOrder;

    @JsonProperty("store_slug")
    private String storeSlug;

    @JsonProperty("category_slug")
    private String categorySlug;

    @JsonProperty("product_slug")
    private String productSlug;

    @JsonProperty("discounted_price")
    private double discountedPrice;

    @JsonProperty("price_with_tax")
    private double priceWithTax;

    @JsonProperty("discount_percentage")
    private String discountPercentage;

    @JsonProperty("inventory_batches")
    private List<InventoryBatchDto> inventoryBatches;

    @JsonProperty("terpenes")
    private List<ProductChemicalPropertiesDto> terpenes;

    @JsonProperty("cannabinoids")
    private List<ProductChemicalPropertiesDto> cannabinoids;

    @JsonProperty("default")
    private boolean isDefault;

    @JsonProperty("sellable")
    private boolean isSellable;

    @JsonProperty("enabled")
    private boolean isEnabled;

    @JsonProperty("tier_type")
    private String tierType;

    @JsonProperty("weight_options")
    private List<String> weightOptions;
}
