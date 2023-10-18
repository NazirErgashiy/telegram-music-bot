package uz.nazir.musicbot.service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrackResponseDto {
    private Long id;
    private String name;
    private String author;
    private String path;
    private LocalDateTime createDate;
}
