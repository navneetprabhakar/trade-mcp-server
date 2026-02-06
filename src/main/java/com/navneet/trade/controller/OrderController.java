package com.navneet.trade.controller;

import com.navneet.trade.models.request.CreateOrderRequest;
import com.navneet.trade.models.response.CreateOrderResponse;
import com.navneet.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author navneet.prabhakar
 */
//@RestController
//@RequestMapping("/v1/orders")
public class OrderController {

  @Autowired private OrderService orderService;

  // Please note this controller is only to test the tool,


  //@PostMapping("/create")
  public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request){
    return orderService.createNewOrder(request);
  }

}
