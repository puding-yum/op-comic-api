package com.pudingyum.opcomic.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChapterDto {
    Long id;

    @JsonProperty(value = "chapter_number")
    private Long chapterNumber;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = "chapter_images")
    private List<ChapterImageDto> chapterImages;
}
