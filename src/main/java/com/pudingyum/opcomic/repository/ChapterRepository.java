package com.pudingyum.opcomic.repository;

import com.pudingyum.opcomic.domain.dao.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Optional<Chapter> findByChapterNumber(Long chapterNumber);
}
