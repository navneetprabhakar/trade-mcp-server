package com.navneet.trade.constants;

/**
 * @author navneet.prabhakar
 */
public enum OrderType {
  MARKET, // An order to buy or sell a security immediately at the best available current price.
  LIMIT, // An order to buy or sell a security at a specific price or better.
  SL, // A stop-loss order is an order to buy or sell a security once the price of the security reaches a specified price, known as the stop price.
  SL_M // A stop-loss market order is an order to buy or sell a security once the price of the security reaches a specified price, known as the stop price. However, unlike a regular stop-loss order, a stop-loss market order does not specify a limit price. Instead, it is executed at the best available market price once the stop price is reached.
}
