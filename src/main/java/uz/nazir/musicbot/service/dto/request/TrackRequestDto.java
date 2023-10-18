package uz.nazir.musicbot.service.dto.request;

import lombok.Data;

@Data
public class TrackRequestDto {
    private String name;
    private String author;
    private String path;
}
