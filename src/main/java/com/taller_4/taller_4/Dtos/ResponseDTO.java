package com.taller_4.taller_4.Dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object response;
}
