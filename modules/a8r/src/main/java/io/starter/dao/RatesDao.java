package io.starter.dao;

import java.util.List;
import java.util.Optional;

import io.starter.entity.RateEntity;
import io.starter.mapper.RateEntityMapper;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.repo.LeaguesRepository;
import io.starter.repo.RatesRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatesDao {

  private final RatesRepository repo;
  private final LeaguesRepository leagueRepo;
  private final RateEntityMapper mapper;

  @Autowired
  public RatesDao(RatesRepository repo,
                  LeaguesRepository leagueRepo,
                  RateEntityMapper mapper) {
    this.repo = repo;
    this.leagueRepo = leagueRepo;
    this.mapper = mapper;
  }

  @Transactional
  public void saveAll(Lines<Currency> data, Long id) {
    leagueRepo.findById(id)
        .flatMap(league -> {
          List<RateEntity> entityList = mapper.apply(data);
          entityList.forEach(entity -> entity.setLeagueId(league));
          repo.saveAll(entityList);
          return Optional.empty();
        });
  }

  public void saveAll(List<RateEntity> entities, Long id) {
    entities.forEach(entity -> entity.setLeagueId(leagueRepo.findById(id).orElseThrow()));
    repo.saveAll(entities);
  }
}
