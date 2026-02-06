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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusResponse {
    private String status;
    private Payload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        @JsonProperty("groww_order_id")
        private String growwOrderId;
        @JsonProperty("order_status")
        private String orderStatus;
        private String remark;
        @JsonProperty("filled_quantity")
        private Long filledQuantity;
        @JsonProperty("order_reference_id")
        private String orderReferenceId;
    }
}
