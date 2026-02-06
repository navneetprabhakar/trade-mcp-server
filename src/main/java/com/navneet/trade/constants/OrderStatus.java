package com.navneet.trade.constants;

/**
 * List at: https://groww.in/trade-api/docs/curl/annexures#order-status
 * @author navneet.prabhakar
 */
public enum OrderStatus {
  NEW,
  ACKED,
  TRIGGER_PENDING,
  APPROVED,
  REJECTED,
  FAILED,
  EXECUTED,
  DELIVERY_AWAITED,
  CANCELLED,
  CANCELLATION_REQUESTED,
  MODIFICATION_REQUESTED,
  COMPLETED
}
