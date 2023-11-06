package uz.nazir.musicbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.nazir.musicbot.repository.dao.file.MusicRepository;
import uz.nazir.musicbot.repository.entity.Track;
import uz.nazir.musicbot.repository.dao.TrackRepository;
import uz.nazir.musicbot.service.dto.request.TrackRequestDto;
import uz.nazir.musicbot.service.dto.response.TrackResponseDto;
import uz.nazir.musicbot.mappers.TrackMapper;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final MusicRepository musicRepository;
    private final TrackMapper trackMapper;

    @Transactional
    public void saveTrack(TrackRequestDto track, File music, Update update) {
        String absolutePath = String.valueOf(musicRepository.save(music, track.getName()));
        if (update.getMessage().getChat().getUserName() != null) {
            track.setAuthor(update.getMessage().getChat().getUserName());
        }else {
            track.setName(update.getMessage().getChat().getFirstName());
        }

        track.setPath(absolutePath);

        Track result = trackMapper.dtoToEntity(track);
        trackRepository.save(result);
    }

    @Transactional(readOnly = true)
    public File getTrack(Long id) {
        Track track = trackRepository.findById(id).get();
        return new File(track.getPath());
    }

    @Transactional(readOnly = true)
    public List<TrackResponseDto> getTrackByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Track> entities = trackRepository.findByName(name, pageable);
        return trackMapper.entityToDto(entities);
    }

    @Transactional(readOnly = true)
    public List<TrackResponseDto> getTrackByName(String name) {
        List<Track> entities = trackRepository.findByName(name);
        return trackMapper.entityToDto(entities);
    }

    @Transactional
    public void deleteTrack(Long id) {
        trackRepository.deleteById(id);
    }

    @Transactional
    public void readTracksFromLocalDirectory(String path) {
        List<File> musicList = musicRepository.readWholeDirectory(path);

        for (File music : musicList) {
            TrackRequestDto currentTrack = new TrackRequestDto();
            currentTrack.setName(music.getName());
            currentTrack.setPath(music.getAbsolutePath());
            currentTrack.setAuthor("_SYSTEM");

            //System.out.println(currentTrack.getName());
            //System.out.println(currentTrack.getPath());

            Track entity = trackMapper.dtoToEntity(currentTrack);
            trackRepository.save(entity);
        }
    }
}
