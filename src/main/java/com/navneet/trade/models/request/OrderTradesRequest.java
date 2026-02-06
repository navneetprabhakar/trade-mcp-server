package com.navneet.trade.models.request;

import com.navneet.trade.constants.Segment;
import lombok.Data;

/**
 * @author navneet.prabhakar
 */
@Data
public class OrderTradesRequest {

  private String orderId;
  private Segment segment;
  private Integer pageNumber;
  private Integer pageSize;

}
