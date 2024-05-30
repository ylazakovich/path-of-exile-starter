package io.starter.telegram.dao;

import java.util.Objects;

import io.starter.telegram.entity.UserEntity;
import io.starter.telegram.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class UserDAO {

  private final UserRepository userRepository;

  @Autowired
  public UserDAO(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void save(UserEntity userEntity) {
    userRepository.save(userEntity);
  }

  public void saveWhenNotExist(final User user) {
    UserEntity userEntity = userRepository.findByUserId(user.getId());
    if (Objects.isNull(userEntity)) {
      userEntity = new UserEntity(user.getId(), user.getUserName());
      save(userEntity);
    }
  }

  public void removeUser(UserEntity userEntity) {
    userRepository.delete(userEntity);
  }
}
