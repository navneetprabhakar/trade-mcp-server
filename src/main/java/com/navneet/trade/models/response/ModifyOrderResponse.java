package com.navneet.trade.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ModifyOrderResponse {
  private String status;
  private Payload payload;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Payload{
    @JsonProperty("groww_order_id")
    private String growwOrderId;
    @JsonProperty("order_status")
    private String orderStatus;
  }
}
