package com.navneet.trade.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.navneet.trade.constants.OrderStatus;
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
public class CreateOrderResponse {

  private String status;
  private Payload payload;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Payload {

    @JsonProperty("groww_order_id")
    private String growwOrderId;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @JsonProperty("order_reference_id")
    private String orderReferenceId;

    private String remark;
  }
}
