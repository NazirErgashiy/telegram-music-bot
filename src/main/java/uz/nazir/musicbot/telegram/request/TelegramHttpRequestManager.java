package uz.nazir.musicbot.telegram.request;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import uz.nazir.musicbot.telegram.request.dto.PostRequestFather;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class TelegramHttpRequestManager {

    private final ObjectMapper objectMapper = new ObjectMapper().configure(JsonGenerator.Feature.IGNORE_UNKNOWN,true);

    public PostRequestFather downloadMusicFromChat(String uri) throws IOException {
        HttpPost post = new HttpPost(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        String json = null;
        if (entity != null) {
            json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }
        return objectMapper.readValue(json, PostRequestFather.class);
    }
}
