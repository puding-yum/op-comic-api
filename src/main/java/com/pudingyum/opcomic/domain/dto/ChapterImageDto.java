package com.pudingyum.opcomic.domain.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pudingyum.opcomic.domain.dao.Chapter;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChapterImageDto {
    private Long id;

    private String url;

    @JsonProperty(value = "page_number")
    private Integer pageNumber;

    @JsonIgnore
    @JsonProperty(value = "chapter_id")
    private Long chapterId;
}
