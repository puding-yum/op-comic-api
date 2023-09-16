package com.pudingyum.opcomic.service;

import com.pudingyum.opcomic.domain.dao.Chapter;
import com.pudingyum.opcomic.domain.dao.ChapterImage;
import com.pudingyum.opcomic.domain.dto.ChapterDto;
import com.pudingyum.opcomic.domain.dto.ChapterImageDto;
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
//            log.info(opSourceUrl+chapterNumber);
            Optional<Chapter> chapterCheck = chapterRepository.findByChapterNumber(chapterNumber);
            if(chapterCheck.isPresent()){
                return Response.build("Chapter exist", null, null, HttpStatusCode.valueOf(400));
            }

            Document doc = Jsoup.connect(opSourceUrl+chapterNumber+"/").get();
            Elements chapterImages = doc.select(".entry-content img");

            Chapter chapter = Chapter.builder().chapterNumber(chapterNumber).chapterImages(new ArrayList<>()).build();
            chapterRepository.save(chapter);

            for(Element image:chapterImages){
                Pattern pattern = Pattern.compile("\\d+$");
                Matcher matcher = pattern.matcher(image.attr("alt"));

                ChapterImage chapterImage = ChapterImage.builder().chapter(chapter).url(image.attr("src")).build();
                while (matcher.find()){
                    chapterImage.setPageNumber(Integer.valueOf(matcher.group()));
                }
                chapterImageRepository.save(chapterImage);
                chapter.getChapterImages().add(chapterImage);
            }

            ChapterDto chapterDto = ChapterDto.builder().id(chapter.getId()).chapterNumber(chapter.getChapterNumber()).build();

            return Response.build("Add one chapter success", chapterDto, null, HttpStatusCode.valueOf(201));
        }catch (IOException e){
            log.error(e.toString());

            return Response.build("Internal server error!", null, null, HttpStatusCode.valueOf(500));
        }
    }

    public ResponseEntity<Object> getOneChapter(Long chapterNumber) {
        try{
            Optional<Chapter> chapter = chapterRepository.findByChapterNumber(chapterNumber);
            if(chapter.isEmpty()){
                return Response.build("Chapter not found", null, null, HttpStatusCode.valueOf(400));
            }

            ChapterDto chapterDto = ChapterDto.builder()
                    .id(chapter.get().getId())
                    .chapterNumber(chapter.get().getChapterNumber())
                    .chapterImages(new ArrayList<>()).build();

            for(ChapterImage chapterImage:chapter.get().getChapterImages()){
//                log.info(chapterImage.getUrl());
                chapterDto.getChapterImages().add(ChapterImageDto.builder()
                        .id(chapterImage.getId())
                        .pageNumber(chapterImage.getPageNumber())
                        .url(chapterImage.getUrl()).build());
            }

            return Response.build("Get one chapter success", chapterDto, null, HttpStatusCode.valueOf(200));
        }catch (Exception e){
            log.error(e.toString());

            return Response.build("Internal server error!", null, null, HttpStatusCode.valueOf(500));
        }
    }

    public ResponseEntity<Object> getChapterList(){
        try{
            List<Chapter> chapters = chapterRepository.findAll();

            List<ChapterDto> chapterDtos = new ArrayList<>();
            for(Chapter chapter:chapters){
                chapterDtos.add(ChapterDto.builder().id(chapter.getId()).chapterNumber(chapter.getChapterNumber()).build());
            }

            return Response.build("Get all chapter success", chapterDtos, null, HttpStatusCode.valueOf(200));
        }catch (Exception e){
            log.error(e.toString());

            return Response.build("Internal server error!", null, null, HttpStatusCode.valueOf(500));
        }
    }
}
