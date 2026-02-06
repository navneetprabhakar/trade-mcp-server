package com.navneet.trade.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionsResponse {

  private String status;
  private Payload payload;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Payload {
    private List<Position> positions;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Position {

    @JsonProperty("trading_symbol")
    private String tradingSymbol;

    @JsonProperty("credit_quantity")
    private Integer creditQuantity;

    @JsonProperty("credit_price")
    private Double creditPrice;

    @JsonProperty("debit_quantity")
    private Integer debitQuantity;

    @JsonProperty("debit_price")
    private Double debitPrice;

    @JsonProperty("carry_forward_credit_quantity")
    private Integer carryForwardCreditQuantity;

    @JsonProperty("carry_forward_credit_price")
    private Double carryForwardCreditPrice;

    @JsonProperty("carry_forward_debit_quantity")
    private Integer carryForwardDebitQuantity;

    @JsonProperty("carry_forward_debit_price")
    private Double carryForwardDebitPrice;

    private String exchange;

    @JsonProperty("symbol_isin")
    private String symbolIsin;

    private Integer quantity;

    private String product;

    @JsonProperty("net_carry_forward_quantity")
    private Integer netCarryForwardQuantity;

    @JsonProperty("net_price")
    private Double netPrice;

    @JsonProperty("net_carry_forward_price")
    private Double netCarryForwardPrice;

    @JsonProperty("realised_pnl")
    private Double realisedPnl;
  }
}
