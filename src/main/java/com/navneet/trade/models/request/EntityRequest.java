package com.navneet.trade.models.request;

import com.navneet.trade.constants.Exchange;
import com.navneet.trade.constants.Segment;
import lombok.Data;

/**
 * @author navneet.prabhakar
 */
@Data
public class EntityRequest {

  private String name;
  private Exchange exchange;
  private Segment segment;

}
