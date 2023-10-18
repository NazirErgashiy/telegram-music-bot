package uz.nazir.musicbot.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nazir.musicbot.repository.entity.UserEntity;
import uz.nazir.musicbot.service.dto.request.UserRequestDto;
import uz.nazir.musicbot.service.dto.response.UserResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "joinedDate", ignore = true)
    @Mapping(target = "lastUsedDate", ignore = true)
    UserEntity dtoToEntity(UserRequestDto requestDto);

    UserResponseDto entityToDto(UserEntity entity);

    List<UserResponseDto> entityToDto(List<UserEntity> entity);
}
