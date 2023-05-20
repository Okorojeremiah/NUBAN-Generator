package com.nuban.nubangenerator.data.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
public class ApiResponse {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private ZonedDateTime timeStamp;
    private int statusCode;
    private String path;
    private  Object data;
    private Boolean isSuccessful;
}
