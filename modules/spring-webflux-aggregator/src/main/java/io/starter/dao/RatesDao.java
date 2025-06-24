package io.starter.dao;

import java.util.List;
import java.util.Optional;

import io.starter.entity.RateEntity;
import io.starter.mapper.RateEntityMapper;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.service.DataAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatesDao {

  private final DataAccessService dataAccessService;
  private final RateEntityMapper mapper;

  @Autowired
  public RatesDao(DataAccessService dataAccessService,
                  RateEntityMapper mapper) {
    this.dataAccessService = dataAccessService;
    this.mapper = mapper;
  }

  @Transactional
  public void saveAll(Lines<Currency> data, Long id) {
    dataAccessService.findLeagueById(id)
        .flatMap(league -> {
          List<RateEntity> entityList = mapper.apply(data);
          entityList.forEach(entity -> entity.setLeagueId(league));
          dataAccessService.saveRates(entityList);
          return Optional.empty();
        });
  }

  public void saveAll(List<RateEntity> entities, Long id) {
    entities.forEach(entity -> entity.setLeagueId(dataAccessService.findLeagueById(id).orElseThrow()));
    dataAccessService.saveRates(entities);
  }
}
