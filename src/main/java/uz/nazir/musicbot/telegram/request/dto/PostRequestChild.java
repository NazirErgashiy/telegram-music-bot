package uz.nazir.musicbot.telegram.request.dto;

import lombok.Data;

@Data
public class PostRequestChild {
    private String file_id;
    private String file_unique_id;
    private float file_size;
    private String file_path;
}
