package com.navneet.trade.controller;

import com.navneet.trade.models.response.TokenResponse;
import com.navneet.trade.service.GrowwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author navneet.prabhakar
 */
@RestController
@RequestMapping("v1/groww")
public class GrowwController {

  @Autowired private GrowwService service;


  @GetMapping("/generate-token")
  public TokenResponse generateToken() {
    return service.getToken();
  }

  @GetMapping("/ingest-instruments")
  public String ingestInstrumentsData(@RequestParam(name = "filepath") String filePath) {
    service.ingestInstrumentsData(filePath);
    return "Ingestion initiated for file: " + filePath;
  }

}
