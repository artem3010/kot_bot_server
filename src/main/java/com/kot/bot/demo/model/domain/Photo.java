package com.kot.bot.demo.model.domain;

import com.kot.bot.demo.exceptions.FilePathEmptyException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.File;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "photo")
public class Photo {

    @Id
    private String id;

    @Field("path")
    private String path;

    @Field("file_id")
    private String fileId;

    public File getFile(){
        if(Strings.isEmpty(path)){
            throw new FilePathEmptyException("File not found" + path);
        }
        return new File(path);
    }


}
