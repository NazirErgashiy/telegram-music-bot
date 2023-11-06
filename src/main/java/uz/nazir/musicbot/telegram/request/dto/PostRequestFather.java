package uz.nazir.musicbot.telegram.request.dto;

import lombok.Data;

@Data
public class PostRequestFather {
    private boolean ok;
    private PostRequestChild postRequestChild;
}
