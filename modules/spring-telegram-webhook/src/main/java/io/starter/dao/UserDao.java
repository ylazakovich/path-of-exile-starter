package io.starter.dao;

import java.util.Objects;

import io.starter.constants.CurrencyDisplay;
import io.starter.constants.League;
import io.starter.entity.LeagueEntity;
import io.starter.entity.UserEntity;
import io.starter.repo.LeagueRepository;
import io.starter.repo.UserRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Service
public class UserDao {

  private final UserRepository userRepository;
  private final LeagueRepository leagueRepository;

  @Autowired
  public UserDao(UserRepository userRepository,
                 LeagueRepository leagueRepository) {
    this.userRepository = userRepository;
    this.leagueRepository = leagueRepository;
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

  public void saveLeague(User user,
                         League setting) {
    UserEntity userEntity = userRepository.findByUserId(user.getId());
    LeagueEntity leagueEntity = leagueRepository.findById(setting.id);
    if (Objects.isNull(leagueEntity)) {
      leagueEntity = resolveDefaultLeague();
    }
    userEntity.setLeague(leagueEntity);
    save(userEntity);
  }

  public LeagueEntity readLeague(User user) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    return entity.getLeague();
  }

  public int readSkillPage(User user) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    return entity.getSkillPage();
  }

  public void saveSkillPage(User user, int page) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    entity.setSkillPage(page);
    save(entity);
  }

  public int readRecipePage(User user) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    return Objects.requireNonNullElse(entity.getRecipePage(), 1);
  }

  public void saveRecipePage(User user, int page) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    entity.setRecipePage(page);
    save(entity);
  }

  public CurrencyDisplay readCurrency(User user) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    if (entity == null || entity.getCurrencyDisplay() == null) {
      return CurrencyDisplay.CHAOS;
    }
    try {
      return CurrencyDisplay.valueOf(entity.getCurrencyDisplay());
    } catch (IllegalArgumentException ignored) {
      return CurrencyDisplay.CHAOS;
    }
  }

  public void saveCurrency(User user, CurrencyDisplay currencyDisplay) {
    UserEntity entity = userRepository.findByUserId(user.getId());
    entity.setCurrencyDisplay(currencyDisplay.name());
    save(entity);
  }

  public void saveWhenNotExist(User user) {
    UserEntity userEntity = userRepository.findByUserId(user.getId());
    LeagueEntity leagueEntity = resolveDefaultLeague();
    if (Objects.isNull(userEntity)) {
      userEntity = new UserEntity();
      userEntity.setLeague(leagueEntity);
      userEntity.setUserId(user.getId());
      userEntity.setFirstName(user.getFirstName());
      userEntity.setUserName(Objects.requireNonNullElse(user.getUserName(), StringUtils.EMPTY));
      userEntity.setLastName(Objects.requireNonNullElse(user.getLastName(), StringUtils.EMPTY));
      userEntity.setSkillPage(1);
      userEntity.setRecipePage(1);
      userEntity.setCurrencyDisplay(CurrencyDisplay.CHAOS.name());
      save(userEntity);
    }
  }

  private LeagueEntity resolveDefaultLeague() {
    LeagueEntity leagueEntity = leagueRepository.findById(League.STANDARD.id);
    if (Objects.isNull(leagueEntity)) {
      leagueEntity = leagueRepository.findByName("Standard");
    }
    if (Objects.isNull(leagueEntity)) {
      leagueEntity = leagueRepository.findAll().stream().findFirst().orElse(null);
    }
    if (Objects.isNull(leagueEntity)) {
      throw new IllegalStateException("No leagues are present in DB, cannot initialize user");
    }
    return leagueEntity;
  }
}
