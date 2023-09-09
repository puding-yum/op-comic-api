package com.pudingyum.opcomic.domain;

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
    private Integer chapterNumber;

    @JsonManagedReference
    @OneToMany(mappedBy="chapter", fetch = FetchType.LAZY)
    private List<ChapterImage> chapterImages;
}
