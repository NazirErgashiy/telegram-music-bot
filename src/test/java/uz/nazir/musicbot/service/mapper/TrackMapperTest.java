package uz.nazir.musicbot.service.mapper;

import org.junit.jupiter.api.Test;
import uz.nazir.musicbot.mappers.TrackMapper;
import uz.nazir.musicbot.mappers.TrackMapperImpl;
import uz.nazir.musicbot.repository.entity.Track;
import uz.nazir.musicbot.service.dto.request.TrackRequestDto;
import uz.nazir.musicbot.service.dto.response.TrackResponseDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TrackMapperTest {

    TrackMapper trackMapper = new TrackMapperImpl();

    @Test
    void dtoToEntity() {
        TrackRequestDto requestDto = new TrackRequestDto();
        requestDto.setName("name");
        requestDto.setPath("A:\\path");
        requestDto.setAuthor("author");

        Track entity = trackMapper.dtoToEntity(requestDto);

        assertEquals(requestDto.getName(), entity.getName());
        assertEquals(requestDto.getPath(), entity.getPath());
        assertEquals(requestDto.getAuthor(), entity.getAuthor());

        assertNull(entity.getId());
        assertNull(entity.getCreateDate());
    }

    @Test
    void entityToDto() {
        Track entity = new Track();
        entity.setId(1L);
        entity.setName("name");
        entity.setPath("A:\\path");
        entity.setAuthor("author");
        entity.setCreateDate(LocalDateTime.now());

        TrackResponseDto responseDto = trackMapper.entityToDto(entity);

        assertEquals(entity.getId(), responseDto.getId());
        assertEquals(entity.getName(), responseDto.getName());
        assertEquals(entity.getPath(), responseDto.getPath());
        assertEquals(entity.getAuthor(), responseDto.getAuthor());
        assertEquals(entity.getCreateDate(), responseDto.getCreateDate());
    }
}