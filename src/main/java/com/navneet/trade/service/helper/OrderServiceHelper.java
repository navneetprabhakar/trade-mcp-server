package com.navneet.trade.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navneet.trade.constants.GrowwConstants;
import com.navneet.trade.models.request.CreateOrderRequest;
import com.navneet.trade.models.response.CreateOrderResponse;
import com.navneet.trade.utils.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author navneet.prabhakar
 */
@Component
@Slf4j
public class OrderServiceHelper {

  @Autowired private GrowwServiceHelper growwServiceHelper;
  @Autowired private RestUtils restUtils;
  @Autowired private GrowwConstants constants;
  private static final ObjectMapper mapper=new ObjectMapper();


  /**
   * Creates a new buy or sell order based on the provided CreateOrderRequest.
   *
   * @param request The CreateOrderRequest containing the details of the order to be created.
   * @return A CreateOrderResponse containing the details of the created order, including its status and any relevant metadata.
   * @throws JsonProcessingException if there is an error processing the JSON response.
   */
  public CreateOrderResponse createNewOrder(CreateOrderRequest request)
      throws JsonProcessingException {
    log.info("Creating new order with request: {}", request);
    ResponseEntity<String> response= restUtils.restPostCall(constants.getBaseUrl()+constants.getCreateOrderUrl(),
        growwServiceHelper.generateHeaders(),null, request);
    if(response.getStatusCode().is2xxSuccessful()){
      return mapper.readValue(response.getBody(), CreateOrderResponse.class);
    }else{
      log.info("Unable to create order, response: {}", response);
      return null;
    }
  }

}
