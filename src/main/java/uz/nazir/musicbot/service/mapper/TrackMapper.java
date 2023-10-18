package uz.nazir.musicbot.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nazir.musicbot.repository.entity.TrackEntity;
import uz.nazir.musicbot.service.dto.request.TrackRequestDto;
import uz.nazir.musicbot.service.dto.response.TrackResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    TrackEntity dtoToEntity(TrackRequestDto requestDto);

    TrackResponseDto entityToDto(TrackEntity entity);

    List<TrackResponseDto> entityToDto(List<TrackEntity> entities);
}
