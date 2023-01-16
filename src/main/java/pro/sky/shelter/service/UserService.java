package pro.sky.shelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.shelter.core.entity.UserEntity;
import pro.sky.shelter.core.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserByIdOrNew(Long id) {
        logger.info("Method getChatByIdOrNew was start for find User by id = {}, or return new User", id);
        UserEntity user = userRepository.getUserEntityByChatId(id);
        if (user == null) {
            logger.debug("Method getUserEntityByChatId will return the new chat");
            user = new UserEntity();
            user.setId(id);
            userRepository.save(user);
            return user;
        }
        logger.debug("Method getChatByIdOrNew will return the found chat");
        return user;
    }

    public UserEntity addUser(Long id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        return addUser(user);
    }

    public UserEntity addUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity findUser(Long id) {
        return userRepository.getUserEntityByChatId(id);
    }

    public void deleteUser(Long id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        deleteUser(user);
    }

    public void deleteUser(UserEntity user) {
        userRepository.delete(user);
    }
}
