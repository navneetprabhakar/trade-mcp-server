package com.navneet.trade.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.navneet.trade.constants.CandleIntervals;
import com.navneet.trade.constants.Exchange;
import com.navneet.trade.constants.Segment;
import lombok.Data;
import lombok.ToString;

/**
 * @author navneet.prabhakar
 */
@Data
@ToString
public class HistoricDataRequest {

  public Exchange exchange;
  public Segment segment;
  @JsonProperty("groww_symbol")
  private String growwSymbol;
  @JsonProperty("start_time")
  private String startTime;
  @JsonProperty("end_time")
  private String endTime;
  @JsonProperty("candle_interval")
  private CandleIntervals candleInterval;

}
