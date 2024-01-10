package io.automation.units.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.automation.model.Lines;
import io.automation.model.Skill;
import io.automation.service.PoeNinjaService;
import io.automation.utils.Generator;
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

    poeNinjaService.getSkills().subscribe(actual -> {
          assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
          assertThat(actual.getBody()).isEqualTo(expected);
        }
    );
  }
}
