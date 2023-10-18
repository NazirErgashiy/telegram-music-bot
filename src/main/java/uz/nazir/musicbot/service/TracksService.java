package uz.nazir.musicbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.nazir.musicbot.repository.MusicRepository;
import uz.nazir.musicbot.repository.entity.TrackEntity;
import uz.nazir.musicbot.repository.sql.TracksRepository;
import uz.nazir.musicbot.service.dto.request.TrackRequestDto;
import uz.nazir.musicbot.service.dto.response.TrackResponseDto;
import uz.nazir.musicbot.service.mapper.TrackMapper;
import uz.nazir.musicbot.service.mapper.TrackMapperImpl;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TracksService {

    private final TracksRepository tracksRepository;
    private final MusicRepository musicRepository;
    private final TrackMapper trackMapper;

    @Transactional
    public void saveTrack(TrackRequestDto track, File music, Update update) {
        String absolutePath = String.valueOf(musicRepository.save(music, track.getName()));
        track.setAuthor(update.getMessage().getChat().getUserName());
        track.setPath(absolutePath);

        TrackEntity result = trackMapper.dtoToEntity(track);
        tracksRepository.save(result);
    }

    @Transactional(readOnly = true)
    public File getTrack(Long id) {
        TrackEntity track = tracksRepository.findById(id).get();
        return new File(track.getPath());
    }

    @Transactional(readOnly = true)
    public List<TrackResponseDto> getTrackByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TrackEntity> entities = tracksRepository.findByName(name, pageable);
        return trackMapper.entityToDto(entities);
    }

    @Transactional(readOnly = true)
    public List<TrackResponseDto> getTrackByName(String name) {
        List<TrackEntity> entities = tracksRepository.findByName(name);
        return trackMapper.entityToDto(entities);
    }

    @Transactional
    public void deleteTrack(Long id) {
        tracksRepository.deleteById(id);
    }

    @Transactional
    public void readTracksFromLocalDirectory(String path) {
        List<File> musicList = musicRepository.readWholeDirectory(path);

        for (File music : musicList) {
            TrackRequestDto currentTrack = new TrackRequestDto();
            currentTrack.setName(music.getName());
            currentTrack.setPath(music.getAbsolutePath());
            currentTrack.setAuthor("_SYSTEM");
            System.out.println(currentTrack.getName());
            System.out.println(currentTrack.getPath());

            TrackEntity entity = trackMapper.dtoToEntity(currentTrack);
            tracksRepository.save(entity);
        }
    }
}
