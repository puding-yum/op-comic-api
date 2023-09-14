package com.pudingyum.opcomic.controller;

import com.pudingyum.opcomic.domain.dao.Chapter;
import com.pudingyum.opcomic.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping(value = "/v1/chapter")
public class ChapterController {
    @Autowired
    ChapterService chapterService;

    @PostMapping(value = "/{chapterNumber}")
    public ResponseEntity<Object> addOneChapter(@PathVariable Long chapterNumber) throws IOException {
        return chapterService.addOneChapter(chapterNumber);
    }

    @GetMapping(value = "/{chapterNumber}")
    public ResponseEntity<Object> getOneChapter(@PathVariable Long chapterNumber) throws IOException {
        return chapterService.getOneChapter(chapterNumber);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAllChapter() throws IOException {
        return chapterService.getChapterList();
    }
}
