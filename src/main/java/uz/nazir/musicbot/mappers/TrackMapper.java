package uz.nazir.musicbot.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nazir.musicbot.repository.entity.Track;
import uz.nazir.musicbot.service.dto.request.TrackRequestDto;
import uz.nazir.musicbot.service.dto.response.TrackResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    Track dtoToEntity(TrackRequestDto requestDto);

    TrackResponseDto entityToDto(Track entity);

    List<TrackResponseDto> entityToDto(List<Track> entities);
}
