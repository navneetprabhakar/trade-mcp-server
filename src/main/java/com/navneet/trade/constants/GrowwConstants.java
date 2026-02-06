package com.navneet.trade.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author navneet.prabhakar
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "groww")
public class GrowwConstants {

  private String baseUrl;
  private String apiKey;
  private String secret;
  private String tokenUrl;
  private String historicDataUrl;
  private String holdingsUrl;
  private String positionsUrl;
  private String positionsTradingUrl;
  private String orderListUrl;
  private String orderDetailsUrl;
  private String createOrderUrl;
  private String modifyOrderUrl;
  private String cancelOrderUrl;
  private String orderStatusUrl;
  private String orderStatusReferenceUrl;
  private String orderTradesUrl;

}
