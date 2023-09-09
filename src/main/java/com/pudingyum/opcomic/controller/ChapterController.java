package com.pudingyum.opcomic.controller;

import com.pudingyum.opcomic.domain.Chapter;
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

    @PostMapping(value = "/add-one")
    public ResponseEntity<Object> addOneChapter(@RequestBody Chapter chapter) throws IOException {
        return chapterService.addOneChapter(chapter);
    }
}
