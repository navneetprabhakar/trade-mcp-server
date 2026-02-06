package com.navneet.trade.constants;

/**
 * @author navneet.prabhakar
 */
public enum ProductType {
  CNC, // Cash and Carry for normal equity delivery trades
  MIS, // Margin Intraday Square-off for intraday trades
  NRML // Normal for non-equity trades like Futures and Options

}
