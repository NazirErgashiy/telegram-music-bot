package uz.nazir.musicbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.nazir.musicbot.config.ConstantsConfig;
import uz.nazir.musicbot.repository.entity.UserEntity;
import uz.nazir.musicbot.repository.sql.UsersRepository;
import uz.nazir.musicbot.repository.util.StandardTime;
import uz.nazir.musicbot.service.dto.request.UserRequestDto;
import uz.nazir.musicbot.service.dto.response.UserResponseDto;
import uz.nazir.musicbot.service.mapper.UserMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Transactional
    public void saveOrUpdateUser(Update message) {
        if (message.hasCallbackQuery()) return;
        if (message.getMessage() == null) return;

        UserRequestDto user = new UserRequestDto();
        user.setName(message.getMessage().getChat().getUserName());
        user.setCode(message.getMessage().getChatId().toString());

        UserEntity foundUser = usersRepository.findByCode(String.valueOf(message.getMessage().getChatId()));
        if (foundUser != null) {
            if (foundUser.getCode() == user.getCode()) {
                foundUser.setLastUsedDate(StandardTime.nowIso8601());
                usersRepository.save(foundUser);
            }
            for (Long admin : ConstantsConfig.ADMIN_ID) {
                if (admin == foundUser.getId()) {
                    System.out.println("AdminName:[" + foundUser.getName() + "]");
                    System.out.println("ChatId:[" + message.getMessage().getChatId() + "]");
                    System.out.println("MessageId:[" + message.getMessage().getMessageId() + "]");
                }
            }
        } else {
            UserEntity savingUser = userMapper.dtoToEntity(user);
            usersRepository.save(savingUser);
        }
    }

    @Transactional
    public void saveUser(UserRequestDto user) {
        UserEntity entity = userMapper.dtoToEntity(user);
        usersRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        List<UserEntity> allUsers = (List<UserEntity>) usersRepository.findAll();

        return userMapper.entityToDto(allUsers);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByChatId(String chatId) {
        UserEntity entity = usersRepository.findByCode(chatId);
        return userMapper.entityToDto(entity);
    }

    @Transactional
    public void saveUserPagination(String chatId, int page) {
        UserEntity entity = usersRepository.findByCode(chatId);
        entity.setPage(page);
        usersRepository.save(entity);
    }

    @Transactional
    public void saveUserSearch(String chatId, String search) {
        UserEntity entity = usersRepository.findByCode(chatId);
        entity.setSearch(search);
        usersRepository.save(entity);
    }
}
