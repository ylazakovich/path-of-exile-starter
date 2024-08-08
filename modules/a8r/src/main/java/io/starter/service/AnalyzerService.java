package io.starter.service;

import java.util.List;
import java.util.Objects;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.dto.SkillDto;
import io.starter.entity.LeagueEntity;
import io.starter.repo.LeaguesRepository;
import io.starter.repo.SkillsRepository;

import org.springframework.stereotype.Service;

@Service
public class AnalyzerService {

  private final SkillsRepository skillsRepository;
  private final LeaguesRepository leaguesRepository;

  public AnalyzerService(SkillsRepository skillsRepository,
                         LeaguesRepository leaguesRepository) {
    this.skillsRepository = skillsRepository;
    this.leaguesRepository = leaguesRepository;
  }

  public List<AnalyzedSkillDto> analyze(String league) {
    LeagueEntity leagueEntity = leaguesRepository.findByName(league);
    List<SkillDto> data = SkillDto.convertToList(skillsRepository.findAllByLeagueId(leagueEntity));
    List<SkillDto> maxQualitySkills = data.stream()
        .filter(skillDto -> skillDto.getVariant().equals("1/20") && !skillDto.isCorrupted())
        .toList();
    List<SkillDto> maxLevelSkills = data.stream()
        .filter(skillDto -> skillDto.getVariant().equals("20") && !skillDto.isCorrupted())
        .toList();
    return subtract(maxQualitySkills, maxLevelSkills);
  }

  private static List<AnalyzedSkillDto> subtract(List<SkillDto> quality, List<SkillDto> level) {
    return quality.stream()
        .filter(qlt -> level.stream()
            .anyMatch(lvl -> lvl.getName().equals(qlt.getName())))
        .map(qlt -> {
          SkillDto matchingLevelGem = level.stream()
              .filter(lvl -> lvl.getName().equals(qlt.getName()))
              .findFirst().orElse(null);
          if (Objects.nonNull(matchingLevelGem)) {
            long leagueId = qlt.getLeagueId();
            String name = qlt.getName();
            double maxQualityPrice = qlt.getChaosEquivalentPrice();
            double maxLevelPrice = matchingLevelGem.getChaosEquivalentPrice();
            // TODO: craftCost should read from currency table
            return new AnalyzedSkillDto(leagueId, name, 1.0, maxQualityPrice - maxLevelPrice);
          }
          return null;
        }).toList();
  }
}
