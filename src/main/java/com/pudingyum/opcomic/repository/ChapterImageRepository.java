package com.pudingyum.opcomic.repository;

import com.pudingyum.opcomic.domain.dao.ChapterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterImageRepository extends JpaRepository<ChapterImage, Long> {
}
