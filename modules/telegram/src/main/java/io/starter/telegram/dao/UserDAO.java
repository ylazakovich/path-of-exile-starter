package io.starter.telegram.dao;

import java.util.List;
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

  public UserEntity findByUserId(long id) {
    return userRepository.findById(id);
  }

  public List<UserEntity> findAll() {
    return userRepository.findAll();
  }

  public void removeUser(UserEntity userEntity) {
    userRepository.delete(userEntity);
  }

  public void save(UserEntity userEntity) {
    userRepository.save(userEntity);
  }

  public void addIfNotExist(final User user) {
    UserEntity userEntity = findByUserId(user.getId());
    if (Objects.isNull(userEntity)) {
      userEntity = new UserEntity(user.getId(), user.getUserName());
      save(userEntity);
    }
  }
}
