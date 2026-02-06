package com.navneet.trade.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.navneet.trade.constants.OrderType;
import com.navneet.trade.constants.Segment;
import lombok.Data;

/**
 * @author navneet.prabhakar
 */
@Data
public class ModifyOrderRequest {

  private Integer quantity;
  private Double price;
  @JsonProperty("trigger_price")
  private Double triggerPrice;
  @JsonProperty("order_type")
  private OrderType orderType;
  private Segment segment;
  @JsonProperty("groww_order_id")
  private String growwOrderId;

}
