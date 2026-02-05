package com.navneet.trade.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author navneet.prabhakar
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoldingsResponse {

  private String status;
  private Payload payload;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Payload {
    private List<Holding> holdings;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Holding {

    private String isin;

    @JsonProperty("trading_symbol")
    private String tradingSymbol;

    private Integer quantity;

    @JsonProperty("average_price")
    private Double averagePrice;

    @JsonProperty("pledge_quantity")
    private Integer pledgeQuantity;

    @JsonProperty("demat_locked_quantity")
    private Integer dematLockedQuantity;

    @JsonProperty("groww_locked_quantity")
    private Double growwLockedQuantity;

    @JsonProperty("repledge_quantity")
    private Double repledgeQuantity;

    @JsonProperty("t1_quantity")
    private Integer t1Quantity;

    @JsonProperty("demat_free_quantity")
    private Integer dematFreeQuantity;

    @JsonProperty("corporate_action_additional_quantity")
    private Integer corporateActionAdditionalQuantity;

    @JsonProperty("active_demat_transfer_quantity")
    private Integer activeDematTransferQuantity;
  }
}
