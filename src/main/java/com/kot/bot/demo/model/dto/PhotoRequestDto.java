package com.kot.bot.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PhotoRequestDto implements Serializable {

    @JsonProperty("chat_id")
    String chatId;

    @JsonProperty("photo")
    String photo;
}
