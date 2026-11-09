package com.semika.learn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaDto implements Serializable {

    @JsonProperty("type")
    private String type;

    @JsonProperty("src")
    private String src;

    @JsonProperty("optimized_media")
    private OptimizedMediaDto optimizedMediaDto;
}
