package com.navneet.trade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author navneet.prabhakar
 */
@Entity
@Table(name = "instruments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Instruments {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "exchange")
  private String exchange;

  @Column(name = "exchange_token")
  private String exchangeToken;

  @Column(name = "trading_symbol")
  private String tradingSymbol;

  @Column(name = "groww_symbol")
  private String growwSymbol;

  @Column(name = "name")
  private String name;

  @Column(name = "instrument_type")
  private String instrumentType;

  @Column(name = "segment")
  private String segment;

  @Column(name = "series")
  private String series;

  @Column(name = "isin")
  private String isin;

  @Column(name = "underlying_symbol")
  private String underlyingSymbol;

  @Column(name = "underlying_exchange_token")
  private String underlyingExchangeToken;

  @Column(name = "expiry_date")
  private String expiryDate;

  @Column(name = "strike_price")
  private Double strikePrice;

  @Column(name = "lot_size")
  private Integer lotSize;

  @Column(name = "tick_size")
  private Double tickSize;

  @Column(name = "freeze_quantity")
  private Integer freezeQuantity;

  @Column(name = "is_reserved")
  private Integer isReserved;

  @Column(name = "buy_allowed")
  private Integer buyAllowed;

  @Column(name = "sell_allowed")
  private Integer sellAllowed;

  @Column(name = "internal_trading_symbol")
  private String internalTradingSymbol;

  @Column(name = "is_intraday")
  private Integer isIntraday;
}
