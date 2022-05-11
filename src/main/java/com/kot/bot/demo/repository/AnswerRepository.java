package com.kot.bot.demo.repository;

import com.kot.bot.demo.model.Answer;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AnswerRepository extends MongoRepository<Answer, String> {

    @Aggregation(pipeline = {"{ $sample: { size: 1 } }"})
    Optional<Answer> getRandomAnswer();

}
