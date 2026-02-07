package com.navneet.trade.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navneet.trade.constants.Exchange;
import com.navneet.trade.constants.Segment;
import com.navneet.trade.constants.TransactionType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author navneet.prabhakar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderTradesResponse {
    private String status;
    private Payload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        @JsonProperty("trade_list")
        private List<Trade> tradeList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Trade {
        private Double price;
        private String isin;
        private Integer quantity;
        @JsonProperty("groww_order_id")
        private String growwOrderId;
        @JsonProperty("groww_trade_id")
        private String growwTradeId;
        @JsonProperty("exchange_trade_id")
        private String exchangeTradeId;
        @JsonProperty("exchange_order_id")
        private String exchangeOrderId;
        @JsonProperty("trade_status")
        private String tradeStatus;
        @JsonProperty("trading_symbol")
        private String tradingSymbol;
        private String remark;
        private Exchange exchange;
        private Segment segment;
        private String product;
        @JsonProperty("transaction_type")
        private TransactionType transactionType;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("trade_date_time")
        private String tradeDatetime;
        @JsonProperty("settlement_number")
        private String settlementNumber;
    }
}
