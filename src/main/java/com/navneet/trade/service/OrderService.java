package com.navneet.trade.service;

import com.navneet.trade.models.request.CreateOrderRequest;
import com.navneet.trade.models.response.CreateOrderResponse;

/**
 * @author navneet.prabhakar
 */
public interface OrderService {

  /**
   * Creates a new buy or sell order based on the provided CreateOrderRequest.
   *
   * @param request The CreateOrderRequest containing the details of the order to be created.
   * @return A CreateOrderResponse containing the details of the created order, including its status and any relevant metadata.
   */
  CreateOrderResponse createNewOrder(CreateOrderRequest request);

}
