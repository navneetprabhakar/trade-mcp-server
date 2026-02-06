package com.navneet.trade.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navneet.trade.constants.GrowwConstants;
import com.navneet.trade.utils.RestUtils;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Helper class to reduce code duplication in OrderServiceImpl
 * @author navneet.prabhakar
 */
@Slf4j
@Component
public class OrderServiceHelper {

  @Autowired private GrowwConstants constants;
  @Autowired private RestUtils restUtils;
  @Autowired private GrowwServiceHelper growwServiceHelper;
  private static final ObjectMapper mapper = new ObjectMapper();

  public <T> T executePostCall(String actionLog, String urlPath, Object request, Class<T> responseType) {
    try {
      log.info(actionLog, request);
      ResponseEntity<String> response = restUtils.restPostCall(constants.getBaseUrl() + urlPath,
          growwServiceHelper.generateHeaders(), null, request);
      return handleResponse(response, responseType, "post");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T executeGetCall(String actionLog, String urlPath, Map<String, String> params, Class<T> responseType) {
    try {
      log.info(actionLog);
      ResponseEntity<String> response = restUtils.restGetCall(constants.getBaseUrl() + urlPath,
          growwServiceHelper.generateHeaders(), params);
      return handleResponse(response, responseType, "get");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T handleResponse(ResponseEntity<String> response, Class<T> responseType, String method) throws JsonProcessingException {
    if (response.getStatusCode().is2xxSuccessful()) {
      return mapper.readValue(response.getBody(), responseType);
    } else {
      log.info("Unable to execute {} call, response: {}", method, response);
      return null;
    }
  }
}
