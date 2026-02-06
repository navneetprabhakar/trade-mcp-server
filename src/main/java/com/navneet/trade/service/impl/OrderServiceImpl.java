package com.navneet.trade.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navneet.trade.models.request.CreateOrderRequest;
import com.navneet.trade.models.response.CreateOrderResponse;
import com.navneet.trade.service.OrderService;
import com.navneet.trade.service.helper.OrderServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author navneet.prabhakar
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

  @Autowired private OrderServiceHelper orderServiceHelper;

  @McpTool(name = "create_new_order", description = "Creates a new buy or sell order based on the provided request.")
  @Override
  public CreateOrderResponse createNewOrder(CreateOrderRequest request) {
    try {
      return orderServiceHelper.createNewOrder(request);
    }  catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
