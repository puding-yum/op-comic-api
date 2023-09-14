package com.pudingyum.opcomic.domain.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "chapter_number")
    @JsonProperty(value = "chapter_number")
    private Long chapterNumber;

    @JsonManagedReference
    @OneToMany(mappedBy="chapter", fetch = FetchType.LAZY)
    @JsonProperty(value = "chapter_images")
    private List<ChapterImage> chapterImages;
}
