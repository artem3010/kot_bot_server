package com.kot.bot.demo.util;

import com.kot.bot.demo.model.Photo;
import com.kot.bot.demo.repository.KotPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class KotBotUtil {

    @Autowired
    KotPhotoRepository kotPhotoRepository;

    @PostConstruct
    public void init(){
        kotPhotoRepository.deleteAll();
        File folder = new File("src/main/resources/cat");
        File[] photos = folder.listFiles();
        for (int i = 0; i < photos.length; i++) {
            kotPhotoRepository.insert(Photo.builder().path(photos[i].getPath()).build());
        }
    }

}
