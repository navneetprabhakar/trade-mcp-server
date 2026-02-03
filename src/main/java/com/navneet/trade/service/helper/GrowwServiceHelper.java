package com.navneet.trade.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navneet.trade.constants.GrowwConstants;
import com.navneet.trade.models.TokenRequest;
import com.navneet.trade.models.TokenResponse;
import com.navneet.trade.utils.GrowwUtils;
import com.navneet.trade.utils.RestUtils;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author navneet.prabhakar
 */
@Service
@Slf4j
public class GrowwServiceHelper {

  @Autowired private GrowwConstants constants;
  @Autowired private GrowwUtils utils;
  @Autowired private RestUtils restUtils;

  private static final ObjectMapper mapper=new ObjectMapper();

  public TokenResponse generateToken() throws JsonProcessingException {
    TokenRequest request=utils.generateTokenRequest();
    log.info("Generated TokenRequest: {}", request);
    ResponseEntity<String> response=restUtils.restPostCall(constants.getBaseUrl()+constants.getTokenUrl(), generateAuthHeaders(),null, request );
    if(response.getStatusCode().is2xxSuccessful()){
      return mapper.readValue(response.getBody(), TokenResponse.class);
    }else{
      log.error("Failed to generate token. Status Code: {}, Response Body: {}", response.getStatusCode(), response.getBody());
      return null;
    }
  }

  private Map<String,String> generateAuthHeaders(){
    return Map.of(
        "Authorization", "Bearer "+constants.getApiKey(),
        "Content-Type","application/json"
    );
  }
}
