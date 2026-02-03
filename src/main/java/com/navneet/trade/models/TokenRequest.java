package com.navneet.trade.models;

import lombok.Builder;
import lombok.Data;

/**
 * @author navneet.prabhakar
 */
@Data
@Builder
public class TokenRequest {

  private String key_type;
  private String checksum;
  private String timestamp;
}
