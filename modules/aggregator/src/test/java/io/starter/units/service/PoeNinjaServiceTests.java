package io.starter.units.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.starter.model.Lines;
import io.starter.model.Skill;
import io.starter.service.PoeNinjaService;
import io.starter.utils.Generator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

public class PoeNinjaServiceTests {

  private final PoeNinjaService poeNinjaService = mock();

  @Test
  void getAllSkillGemsTest() {
    Lines<Skill> expected = Generator.generateLineWithSkills();
    when(poeNinjaService.getSkills()).thenReturn(Mono.just(ResponseEntity.ok(expected)));

    poeNinjaService.getSkills().subscribe(
        actual -> {
          assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
          assertThat(actual.getBody()).isEqualTo(expected);
        }
    );
  }
}
