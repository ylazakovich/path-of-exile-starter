package io.automation.controller;

import io.automation.service.SkillGemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

  private final SkillGemService skillGemService;

  public MessageController(SkillGemService skillGemService) {
    this.skillGemService = skillGemService;
  }

  @GetMapping("/gems")
  public String buildMessageWithGems() {
    // TODO: continue
    return null;
  }
}
