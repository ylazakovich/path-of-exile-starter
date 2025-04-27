package io.starter.service;

import java.util.List;
import java.util.Optional;

import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;
import io.starter.entity.SkillEntity;
import io.starter.repo.LeaguesRepository;
import io.starter.repo.RatesRepository;
import io.starter.repo.SkillsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataAccessService {

  private final LeaguesRepository leaguesRepository;
  private final SkillsRepository skillsRepository;
  private final RatesRepository ratesRepository;

  @Autowired
  public DataAccessService(LeaguesRepository leaguesRepository,
                           SkillsRepository skillsRepository,
                           RatesRepository ratesRepository) {
    this.leaguesRepository = leaguesRepository;
    this.skillsRepository = skillsRepository;
    this.ratesRepository = ratesRepository;
  }

  @Transactional(readOnly = true)
  public List<SkillEntity> findSkillsByLeague(LeagueEntity league) {
    return skillsRepository.findAllByLeagueId(league);
  }

  @Transactional(readOnly = true)
  public Optional<RateEntity> findRateByNameAndLeague(String name, LeagueEntity league) {
    return Optional.ofNullable(ratesRepository.findByNameAndLeagueId(name, league));
  }

  @Transactional(readOnly = true)
  public List<RateEntity> findRatesByLeague(LeagueEntity league) {
    return ratesRepository.findAllByLeagueId(league);
  }

  @Transactional(readOnly = true)
  public Optional<LeagueEntity> findLeagueById(Long id) {
    return leaguesRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public LeagueEntity findLeagueByName(String name) {
    return leaguesRepository.findByName(name);
  }

  @Transactional
  public void saveSkills(List<SkillEntity> skills) {
    skillsRepository.saveAll(skills);
  }
}
