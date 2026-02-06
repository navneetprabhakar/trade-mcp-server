package com.navneet.trade.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OrderListResponse {
    private String status;
    private Payload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        @JsonProperty("order_list")
        private List<Order> orderList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        @JsonProperty("groww_order_id")
        private String growwOrderId;
        @JsonProperty("trading_symbol")
        private String tradingSymbol;
        @JsonProperty("order_status")
        private String orderStatus;
        private String remark;
        private Long quantity;
        private Long price;
        @JsonProperty("trigger_price")
        private Long triggerPrice;
        @JsonProperty("filled_quantity")
        private Long filledQuantity;
        @JsonProperty("remaining_quantity")
        private Long remainingQuantity;
        @JsonProperty("average_fill_price")
        private Long averageFillPrice;
        @JsonProperty("deliverable_quantity")
        private Long deliverableQuantity;
        @JsonProperty("amo_status")
        private String amoStatus;
        private String validity;
        private String exchange;
        @JsonProperty("order_type")
        private String orderType;
        @JsonProperty("transaction_type")
        private String transactionType;
        private String segment;
        private String product;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("exchange_time")
        private String exchangeTime;
        @JsonProperty("trade_date")
        private String tradeDate;
        @JsonProperty("order_reference_id")
        private String orderReferenceId;
    }
}
