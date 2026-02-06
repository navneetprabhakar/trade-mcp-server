package com.navneet.trade.controller;

import com.navneet.trade.models.request.CreateOrderRequest;
import com.navneet.trade.models.response.CreateOrderResponse;
import com.navneet.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author navneet.prabhakar
 */
@RestController
@RequestMapping("/v1/orders")
public class OrderController {

  @Autowired private OrderService orderService;


  @PostMapping("/create")
  public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request){
    return orderService.createNewOrder(request);
  }

}
