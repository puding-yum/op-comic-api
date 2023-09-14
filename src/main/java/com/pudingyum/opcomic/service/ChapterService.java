package com.pudingyum.opcomic.service;

import com.pudingyum.opcomic.domain.dao.Chapter;
import com.pudingyum.opcomic.domain.dao.ChapterImage;
import com.pudingyum.opcomic.repository.ChapterImageRepository;
import com.pudingyum.opcomic.repository.ChapterRepository;
import com.pudingyum.opcomic.utils.Response;
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
import java.util.Optional;
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

    public ResponseEntity<Object> addOneChapter(Long chapterNumber) throws IOException {
        try{
            log.info(opSourceUrl+chapterNumber);
            Document doc = Jsoup.connect(opSourceUrl+chapterNumber+"/").get();
            Elements chapterImages = doc.select(".entry-content img");

            Chapter chapter1 = Chapter.builder().chapterNumber(chapterNumber).chapterImages(new ArrayList<>()).build();
            chapterRepository.save(chapter1);

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

            return Response.build("Add one chapter success", chapter1, null, HttpStatusCode.valueOf(201));
        }catch (IOException e){
            log.error(e.toString());

            return Response.build("Internal server error!", null, null, HttpStatusCode.valueOf(500));
        }
    }

    public ResponseEntity<Object> getOneChapter(Long chapterNumber) {
        try{
            Optional<Chapter> chapter1 = chapterRepository.findByChapterNumber(chapterNumber);
            if(chapter1.isEmpty()){
                return Response.build("Chapter not found", null, null, HttpStatusCode.valueOf(400));
            }

            return Response.build("Get one chapter success", chapter1, null, HttpStatusCode.valueOf(200));
        }catch (Exception e){
            log.error(e.toString());

            return Response.build("Internal server error!", null, null, HttpStatusCode.valueOf(500));
        }
    }

    public ResponseEntity<Object> getChapterList(){
        try{
            List<Chapter> chapters = chapterRepository.findAll();

            return Response.build("Get all chapter success", chapters, null, HttpStatusCode.valueOf(200));
        }catch (Exception e){
            log.error(e.toString());

            return Response.build("Internal server error!", null, null, HttpStatusCode.valueOf(500));
        }
    }
}
