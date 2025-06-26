package com.homehub_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestMediaDTO {
    private String url;
    private String filename;
    private String mediaType;
    private String mimeType;
    private LocalDateTime createdAt;
}