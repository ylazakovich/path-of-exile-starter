package io.starter.telegram.dao;

import java.util.Objects;

import io.starter.telegram.entity.UserEntity;
import io.starter.telegram.repo.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class UserDao {

  private final UserRepository userRepository;

  @Autowired
  public UserDao(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void save(UserEntity userEntity) {
    userRepository.save(userEntity);
  }

  public void saveLastMessageId(User user,
                                Message message) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    entity.setLastMessageId(message.getMessageId());
    save(entity);
  }

  public void saveLastMessageId(User user,
                                MaybeInaccessibleMessage message) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    entity.setLastMessageId(message.getMessageId());
    save(entity);
  }

  public void saveWhenNotExist(User user) {
    UserEntity userEntity = userRepository.findByUserId(user.getId());
    if (Objects.isNull(userEntity)) {
      userEntity = new UserEntity();
      userEntity.setUserName(Objects.requireNonNullElse(user.getUserName(), StringUtils.EMPTY));
      userEntity.setFirstName(Objects.requireNonNullElse(user.getFirstName(), StringUtils.EMPTY));
      userEntity.setLastName(Objects.requireNonNullElse(user.getLastName(), StringUtils.EMPTY));
      userEntity.setUserId(user.getId());
      save(userEntity);
    }
  }
}
