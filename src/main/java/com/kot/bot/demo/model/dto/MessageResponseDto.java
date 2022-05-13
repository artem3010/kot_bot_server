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
public class MessageResponseDto implements Serializable {

    @JsonProperty("ok")
    private boolean ok;

    @JsonProperty("result")
    private ResultMessageResponseDto result;



}
