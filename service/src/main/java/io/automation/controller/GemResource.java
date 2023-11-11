package io.automation.controller;

import java.util.List;

import io.automation.entity.Gem;
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

@RestController
@RequestMapping("/gem")
public class GemResource {

  private final GemService gemService;

  public GemResource(GemService gemService) {
    this.gemService = gemService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<Gem>> getAllGems() {
    return new ResponseEntity<>(gemService.findAllGems(), HttpStatus.OK);
  }

  @GetMapping("/find/{id}")
  public ResponseEntity<Gem> getGemById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(gemService.findGemById(id), HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<Gem> addGem(@RequestBody Gem gem) {
    return new ResponseEntity<>(gemService.addGem(gem), HttpStatus.OK);
  }

  @PutMapping("/update")
  public ResponseEntity<Gem> updateGem(@RequestBody Gem gem) {
    return new ResponseEntity<>(gemService.updateGem(gem), HttpStatus.OK);
  }

  @PutMapping("/delete/{id}")
  public ResponseEntity<?> updateGem(@PathVariable Long id) {
    gemService.deleteGem(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
