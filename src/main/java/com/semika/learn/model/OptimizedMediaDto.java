package com.semika.learn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptimizedMediaDto implements Serializable {

     @JsonProperty("streaming_url")
     private String streamingUrl;

     @JsonProperty("video_thumbnails")
     @JsonIgnore
     private List<ImageThumbnailDto> videoThumbnai;

     @JsonProperty("image_thumbnails")
     private List<ImageThumbnailDto> imageThumbnailDtoList;
}
