package com.kot.bot.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultMessageResponseDto implements Serializable {

    @JsonProperty("message_id")
    private Long messageId;

    @JsonProperty("photo")
    private PhotoResponseDto[] photo;

}
