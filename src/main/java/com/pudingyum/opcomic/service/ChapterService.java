package com.pudingyum.opcomic.service;

import com.pudingyum.opcomic.domain.Chapter;
import com.pudingyum.opcomic.domain.ChapterImage;
import com.pudingyum.opcomic.repository.ChapterImageRepository;
import com.pudingyum.opcomic.repository.ChapterRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ChapterService {
    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    ChapterImageRepository chapterImageRepository;

    @Value("${op-source-url}")
    String opSourceUrl;

    public ResponseEntity<Object> addOneChapter(Chapter chapter) throws IOException {
        try{

            Document doc = Jsoup.connect(opSourceUrl+chapter.getChapterNumber()).get();
            Elements chapterImages = doc.select(".entry-content img");

            Chapter chapter1 = Chapter.builder().chapterNumber(chapter.getChapterNumber()).chapterImages(new ArrayList<>()).build();
            chapterRepository.save(chapter1);

            List<ChapterImage> listChapterImages = new ArrayList<>();

            for(Element image:chapterImages){
                Pattern pattern = Pattern.compile("\\d+$");
                Matcher matcher = pattern.matcher(image.attr("alt"));

                ChapterImage chapterImage = ChapterImage.builder().chapter(chapter1).url(image.attr("src")).build();
                while (matcher.find()){
                    chapterImage.setPage(Integer.valueOf(matcher.group()));
                }
                chapterImageRepository.save(chapterImage);
                chapter1.getChapterImages().add(chapterImage);
            }

            ResponseEntity<Object> responseEntity = new ResponseEntity<>(chapter1, HttpStatusCode.valueOf(201));
            return responseEntity;
        }catch (IOException e){
            log.error(e.toString());

            ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatusCode.valueOf(500));
            return responseEntity;
        }
    }
}
