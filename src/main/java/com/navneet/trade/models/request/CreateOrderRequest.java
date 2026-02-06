package com.navneet.trade.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.navneet.trade.constants.Exchange;
import com.navneet.trade.constants.OrderType;
import com.navneet.trade.constants.ProductType;
import com.navneet.trade.constants.Segment;
import com.navneet.trade.constants.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author navneet.prabhakar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

  @JsonProperty("trading_symbol")
  private String tradingSymbol;

  private Integer quantity;

  private Double price;

  @JsonProperty("trigger_price")
  private Double triggerPrice;

  private String validity;

  private Exchange exchange;

  private Segment segment;

  private ProductType product;

  @JsonProperty("order_type")
  private OrderType orderType;

  @JsonProperty("transaction_type")
  private TransactionType transactionType;

  @JsonProperty("order_reference_id")
  private String orderReferenceId;
}
