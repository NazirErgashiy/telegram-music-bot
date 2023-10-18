package uz.nazir.musicbot.service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String code;
    private String search;
    private Integer page;
    private LocalDateTime joinedDate;
    private LocalDateTime lastUsedDate;
}
