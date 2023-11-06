package uz.nazir.musicbot.repository.dao.file;

import org.springframework.stereotype.Repository;
import uz.nazir.musicbot.config.ConstantsConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class MusicRepository {

    public String save(File music, String name) {
        File file = new File(ConstantsConfig.PATH_TO_SAVE_MUSIC_FROM_USERS + name);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] bytes = read(music);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file.getAbsolutePath();
    }

    public byte[] read(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int l = 0;
            while ((l = fileInputStream.read(buffer)) > 0) {
            }
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<File> readWholeDirectory(String path) {
        try (Stream<Path> stream = Files.walk(Paths.get(path))) {
            List<File> filesInFolder = stream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".mp3") || file.getName().endsWith(".flac") || file.getName().endsWith(".m4a"))//You can add your own types of song
                    .collect(Collectors.toList());
            return filesInFolder;
        } catch (IOException e) {
            System.out.println(e);
        }
        throw new RuntimeException("Can't read folder");
    }
}
