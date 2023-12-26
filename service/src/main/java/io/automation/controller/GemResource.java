package io.automation.controller;

import java.util.List;

import io.automation.service.PoeNinjaService;
import io.automation.dto.GemDTO;
import io.automation.entity.GemEntity;
import io.automation.service.GemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gem")
public class GemResource {

  private final GemService gemService;
  private final PoeNinjaService poeNinjaService;

  public GemResource(GemService gemService, PoeNinjaService client) {
    this.gemService = gemService;
    this.poeNinjaService = client;
  }

  @GetMapping("/load")
  public void loadDataToDatabase() {
    Mono<GemDTO> dataWithGems = poeNinjaService.getDataWithGems();
    dataWithGems.subscribe(body -> gemService.saveAll(body));
  }

  @GetMapping("/all")
  public ResponseEntity<List<GemEntity>> getAllGems() {
    return new ResponseEntity<>(gemService.findAllGems(), HttpStatus.OK);
  }

  @GetMapping("/find/{id}")
  public ResponseEntity<GemEntity> getGemById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(gemService.findGemById(id), HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<GemEntity> addGem(@RequestBody GemEntity gemEntity) {
    return new ResponseEntity<>(gemService.addGem(gemEntity), HttpStatus.OK);
  }

  @PutMapping("/update")
  public ResponseEntity<GemEntity> updateGem(@RequestBody GemEntity gemEntity) {
    gemService.updateGem(gemEntity);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/delete/{id}")
  public ResponseEntity<?> updateGem(@PathVariable Long id) {
    gemService.deleteGem(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
