package io.automation.controller;

import io.automation.service.SkillsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

  private final SkillsService skillsService;

  public MessageController(SkillsService skillsService) {
    this.skillsService = skillsService;
  }

  @GetMapping("/skills")
  public String buildMessageWithGems() {
    // TODO: continue
    return null;
  }
}
