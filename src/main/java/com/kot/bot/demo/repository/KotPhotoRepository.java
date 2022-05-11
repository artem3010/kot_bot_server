package com.kot.bot.demo.repository;

import com.kot.bot.demo.model.Photo;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface KotPhotoRepository extends MongoRepository<Photo, String> {

    @Aggregation(pipeline = {"{ $sample: { size: 1 } }"})
    Optional<Photo> getRandomPhoto();

}
