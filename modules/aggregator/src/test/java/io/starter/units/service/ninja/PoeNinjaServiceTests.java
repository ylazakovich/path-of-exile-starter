package io.starter.units.service.ninja;

import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.service.PoeNinjaService;
import io.starter.utils.Generator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PoeNinjaServiceTests {

  private final PoeNinjaService poeNinjaService = mock();

  @Test
  void testServiceGetSkillsMethod() {
    final Lines<Skill> expectedBody = Generator.generateLineWithSkills();
    when(poeNinjaService.getSkills()).thenReturn(Mono.just(ResponseEntity.ok(expectedBody)));

    poeNinjaService.getSkills().subscribe(
        actual -> {
          assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
          assertThat(actual.getBody()).isEqualTo(expectedBody);
        }
    );
  }
}
