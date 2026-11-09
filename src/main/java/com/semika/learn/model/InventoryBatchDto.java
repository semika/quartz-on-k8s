package com.semika.learn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryBatchDto {

  @JsonProperty("id")
  private String id;

  @JsonProperty("batch_id")
  private String batchId; // For FlowHub this will be a 'FlowHub productId'

  @JsonProperty("sku")
  private String sku;

  @JsonProperty("quantity")
  private BigDecimal quantity;

  @JsonProperty("product_variant")
  private String variantId;

  @JsonProperty("updated_at")
  private String updatedAt;

  @JsonProperty("created_at")
  private String createdAt;
}
