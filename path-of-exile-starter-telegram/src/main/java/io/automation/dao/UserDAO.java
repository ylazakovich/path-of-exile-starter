package io.automation.dao;

import java.util.List;

import io.automation.entity.UserEntity;
import io.automation.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public boolean isExist(long id) {
    UserEntity userEntity = findByUserId(id);
    return userEntity != null;
  }
}
