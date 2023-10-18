package uz.nazir.musicbot.service.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {
    private String name;
    private String code;
    private String search;
    private Integer page;
}
