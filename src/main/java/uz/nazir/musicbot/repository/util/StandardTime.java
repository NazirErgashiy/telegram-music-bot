package uz.nazir.musicbot.repository.util;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

@Component
public class StandardTime {
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";
    private static DateFormat df = new SimpleDateFormat(PATTERN);

    static {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);
    }

    public static LocalDateTime nowIso8601() {
        String nowAsISO = df.format(new Date());
        return LocalDateTime.parse(nowAsISO, DateTimeFormatter.ofPattern(PATTERN));
    }
}
