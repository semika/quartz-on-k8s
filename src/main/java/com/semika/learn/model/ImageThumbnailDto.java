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
public class ImageThumbnailDto implements Serializable {

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("url")
    private String url;

    @JsonProperty("mime_type")
    private String mimeType;
}
