package com.navneet.trade.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.navneet.trade.constants.Segment;
import lombok.Data;

/**
 * @author navneet.prabhakar
 */
@Data
public class OrderStatusRequest {
  private Segment segment;
  @JsonProperty("groww_order_id")
  private String growwOrderId;
}
