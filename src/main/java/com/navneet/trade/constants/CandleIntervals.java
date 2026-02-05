package com.navneet.trade.constants;

import lombok.Getter;

/**
 * @author navneet.prabhakar
 */
@Getter
public enum CandleIntervals {
  ONE_MINUTE("1minute"),
  TWO_MINUTES("2minute"),
  THREE_MINUTES("3minute"),
  FIVE_MINUTES("5minute"),
  TEN_MINUTES("10minute"),
  FIFTEEN_MINUTES("15minute"),
  THIRTY_MINUTES("30minute"),
  ONE_HOUR("1hour"),
  FOUR_HOURS("4hour"),
  ONE_DAY("1day"),
  ONE_WEEK("1week"),
  ONE_MONTH("1month");

  private final String interval;

  CandleIntervals(String interval) {
    this.interval = interval;
  }

}
