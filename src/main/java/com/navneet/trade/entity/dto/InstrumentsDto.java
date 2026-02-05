package com.navneet.trade.entity.dto;

import com.navneet.trade.entity.Instruments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author navneet.prabhakar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstrumentsDto {

  private Long id;
  private String exchange;
  private String exchangeToken;
  private String tradingSymbol;
  private String growwSymbol;
  private String name;
  private String instrumentType;
  private String segment;
  private String series;
  private String isin;
  private String underlyingSymbol;
  private String underlyingExchangeToken;
  private String expiryDate;
  private Double strikePrice;
  private Integer lotSize;
  private Double tickSize;
  private Integer freezeQuantity;
  private Integer isReserved;
  private Integer buyAllowed;
  private Integer sellAllowed;
  private String internalTradingSymbol;
  private Integer isIntraday;

  /**
   * Convert Instruments entity to InstrumentsDto
   * @param instruments Instruments entity
   * @return InstrumentsDto
   */
  public static InstrumentsDto fromEntity(Instruments instruments) {
    return InstrumentsDto.builder()
        .id(instruments.getId())
        .exchange(instruments.getExchange())
        .exchangeToken(instruments.getExchangeToken())
        .tradingSymbol(instruments.getTradingSymbol())
        .growwSymbol(instruments.getGrowwSymbol())
        .name(instruments.getName())
        .instrumentType(instruments.getInstrumentType())
        .segment(instruments.getSegment())
        .series(instruments.getSeries())
        .isin(instruments.getIsin())
        .underlyingSymbol(instruments.getUnderlyingSymbol())
        .underlyingExchangeToken(instruments.getUnderlyingExchangeToken())
        .expiryDate(instruments.getExpiryDate())
        .strikePrice(instruments.getStrikePrice())
        .lotSize(instruments.getLotSize())
        .tickSize(instruments.getTickSize())
        .freezeQuantity(instruments.getFreezeQuantity())
        .isReserved(instruments.getIsReserved())
        .buyAllowed(instruments.getBuyAllowed())
        .sellAllowed(instruments.getSellAllowed())
        .internalTradingSymbol(instruments.getInternalTradingSymbol())
        .isIntraday(instruments.getIsIntraday())
        .build();
  }

}
