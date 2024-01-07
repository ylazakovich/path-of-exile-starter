package io.automation.controller;

import io.automation.service.GemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

  private final GemService gemService;

  public MessageController(GemService gemService) {
    this.gemService = gemService;
  }

  @GetMapping("/gems")
  public String buildMessageWithGems() {
    // TODO: continue
    return null;
  }
}
