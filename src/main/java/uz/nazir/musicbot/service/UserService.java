package uz.nazir.musicbot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.nazir.musicbot.config.ConstantsConfig;
import uz.nazir.musicbot.mappers.UserMapper;
import uz.nazir.musicbot.repository.dao.UserRepository;
import uz.nazir.musicbot.repository.entity.User;
import uz.nazir.musicbot.repository.util.StandardTime;
import uz.nazir.musicbot.service.dto.request.UserRequestDto;
import uz.nazir.musicbot.service.dto.response.UserResponseDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserService {

    private static final Logger slf4jLogger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void saveOrUpdateUser(Update message) {
        if (message.hasCallbackQuery()) return;
        if (message.getMessage() == null) return;

        UserRequestDto user = new UserRequestDto();
        if (message.getMessage().getChat().getUserName() != null) {
            user.setName(message.getMessage().getChat().getUserName());
        } else {
            user.setName(message.getMessage().getChat().getFirstName());
        }
        user.setCode(message.getMessage().getChatId().toString());

        User foundUser = userRepository.findByCode(String.valueOf(message.getMessage().getChatId()));
        if (foundUser != null) {
            if (foundUser.getCode() == user.getCode()) {
                foundUser.setLastUsedDate(StandardTime.nowIso8601());
                userRepository.save(foundUser);
            }
            for (Long admin : ConstantsConfig.ADMIN_ID) {
                if (admin == foundUser.getId()) {
                    slf4jLogger.info("AdminName:[" + foundUser.getName() + "]");
                    slf4jLogger.info("ChatId:[" + message.getMessage().getChatId() + "]");
                    slf4jLogger.info("MessageId:[" + message.getMessage().getMessageId() + "]");
                }
            }
        } else {
            User savingUser = userMapper.dtoToEntity(user);
            userRepository.save(savingUser);
        }
    }

    @Transactional
    public void saveUser(UserRequestDto user) {
        User entity = userMapper.dtoToEntity(user);
        userRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        List<User> allUsers = (List<User>) userRepository.findAll();

        return userMapper.entityToDto(allUsers);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByChatId(String chatId) {
        User entity = userRepository.findByCode(chatId);
        return userMapper.entityToDto(entity);
    }

    @Transactional
    public void saveUserPagination(String chatId, int page) {
        User entity = userRepository.findByCode(chatId);
        entity.setPage(page);
        userRepository.save(entity);
    }

    @Transactional
    public void saveUserSearch(String chatId, String search) {
        User entity = userRepository.findByCode(chatId);
        entity.setSearch(search);
        userRepository.save(entity);
    }
}
