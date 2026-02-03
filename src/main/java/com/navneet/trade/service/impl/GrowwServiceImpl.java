package com.navneet.trade.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navneet.trade.models.TokenResponse;
import com.navneet.trade.service.GrowwService;
import com.navneet.trade.service.helper.GrowwServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author navneet.prabhakar
 */
@Service
@Slf4j
public class GrowwServiceImpl implements GrowwService {

  @Autowired private GrowwServiceHelper helper;

  @Override
  public TokenResponse generateToken() {
    log.info("Generating new token for Groww APIs");
    try {
      return helper.generateToken();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
