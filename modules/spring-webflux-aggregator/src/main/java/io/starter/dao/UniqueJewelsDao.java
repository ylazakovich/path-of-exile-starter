package io.starter.dao;

import java.util.List;
import java.util.Optional;

import io.starter.entity.UniqueJewelEntity;
import io.starter.mapper.UniqueJewelMapper;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.UniqueJewel;
import io.starter.service.DataAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UniqueJewelsDao {

  private final DataAccessService dataAccessService;
  private final UniqueJewelMapper mapper;

  @Autowired
  public UniqueJewelsDao(DataAccessService dataAccessService,
                         UniqueJewelMapper mapper) {
    this.dataAccessService = dataAccessService;
    this.mapper = mapper;
  }

  @Transactional
  public void saveAll(Lines<UniqueJewel> data, Long id) {
    dataAccessService.findLeagueById(id)
        .flatMap(league -> {
          List<UniqueJewelEntity> entityList = mapper.apply(data);
          entityList.forEach(entity -> entity.setLeague(league));
          dataAccessService.saveUniqueJewels(entityList);
          return Optional.empty();
        });
  }

  public void saveAll(List<UniqueJewelEntity> entities, Long id) {
    entities.forEach(entity -> entity.setLeague(dataAccessService.findLeagueById(id).orElseThrow()));
    dataAccessService.saveUniqueJewels(entities);
  }
}
