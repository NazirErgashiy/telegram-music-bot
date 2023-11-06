package uz.nazir.musicbot.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nazir.musicbot.repository.entity.User;
import uz.nazir.musicbot.service.dto.request.UserRequestDto;
import uz.nazir.musicbot.service.dto.response.UserResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "joinedDate", ignore = true)
    @Mapping(target = "lastUsedDate", ignore = true)
    User dtoToEntity(UserRequestDto requestDto);

    UserResponseDto entityToDto(User entity);

    List<UserResponseDto> entityToDto(List<User> entity);
}
