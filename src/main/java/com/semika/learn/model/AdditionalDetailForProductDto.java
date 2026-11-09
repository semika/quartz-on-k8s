package com.semika.learn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Data
@Builder
public class AdditionalDetailForProductDto {

  @JsonProperty("additional_description")
  private String additionalDescription;

  /** Grower */
  private String grower;

  /** Region */
  private String region;

  /** Growing method */
  @JsonProperty("growing_method")
  private String growingMethod;

  /** Harvest */
  private String harvest;

  /** Certificate */
  private String certificate;

  @JsonProperty("certificate_src")
  private String certificateSrc;

  /** Html content */
  @JsonProperty("html_content")
  private String htmlContent;
}
