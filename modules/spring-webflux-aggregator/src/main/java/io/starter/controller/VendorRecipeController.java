package io.starter.controller;

import io.starter.service.DataAccessService;
import io.starter.service.VendorRecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendor-recipes")
@RequiredArgsConstructor
@Log4j2
public class VendorRecipeController {

  private final DataAccessService dataAccessService;
  private final VendorRecipeService vendorRecipeService;
}
