package com.navneet.trade.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navneet.trade.constants.Segment;
import com.navneet.trade.entity.Instruments;
import com.navneet.trade.entity.dto.InstrumentsDto;
import com.navneet.trade.models.request.EntityRequest;
import com.navneet.trade.models.request.HistoricDataRequest;
import com.navneet.trade.models.response.HistoricDataResponse;
import com.navneet.trade.models.response.HoldingsResponse;
import com.navneet.trade.models.response.PositionsResponse;
import com.navneet.trade.models.response.TokenResponse;
import com.navneet.trade.service.GrowwService;
import com.navneet.trade.service.helper.GrowwServiceHelper;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author navneet.prabhakar
 */
@Service
@Slf4j
public class GrowwServiceImpl implements GrowwService {

  @Autowired private GrowwServiceHelper helper;


  @Override
  public TokenResponse getToken() {
    try{
      TokenResponse cache= helper.getTokenFromCache();
      if(null!=cache){
        log.info("Token retrieved from cache successfully");
        return cache;
      }else{
        log.info("No valid token found in cache, generating new token");
        return helper.generateToken();
      }
    }catch (JsonProcessingException e){
      log.error("Error generating token: {}", e.getMessage());
    }
    return null;
  }

  @McpTool(name = "fetch_historic_data",
      description = "Retrieves historical candlestick data (OHLCV - Open, High, Low, Close, Volume) for a "
          + "specific financial instrument from Groww API. "
          + "Supports multiple time intervals (1m, 5m, 15m, 30m, 1h, 1d, 1w, 1M) and date ranges for "
          + "technical analysis and charting purposes.")
  @Override
  public HistoricDataResponse getHistoricData(@McpToolParam(description = "Request containing symbol, "
      + "exchange, interval, from and to dates for fetching historical candlestick data") HistoricDataRequest request) {
    try {
      return helper.fetchHistoricData(request);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void ingestInstrumentsData(String filePath) {
    try {
      int totalRecords=helper.ingestInstrumentsFromCsv(filePath, 2000);
      log.info("Total records ingested: {}", totalRecords);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @McpTool(name="fetch_entities",
      description="Searches and retrieves financial instruments (stocks, derivatives, commodities) from the "
          + "database based on partial name matching, exchange (NSE, BSE, MCX), "
          + "and segment (CASH, FNO, COMMODITY). Returns detailed instrument information including"
          + " trading symbols, lot sizes, tick sizes, and trading permissions.")
  @Override
  public List<InstrumentsDto> fetchEntities(@McpToolParam(description = "Request containing name of "
      + "stock, exchange, and segment (CASH, FNO) for searching instruments with similar names") EntityRequest request) {
    List<Instruments> entities= helper.fetchInstruments(request);
    if(!CollectionUtils.isEmpty(entities)){
      return entities.stream().map(InstrumentsDto::fromEntity).toList();
    }else{
      log.info("No entities found matching the request criteria: {}", request);
      return List.of();
    }
  }

  @McpTool(name="fetch_holdings",
      description="Retrieves the current holdings of financial instruments (stocks, derivatives, commodities) "
          + "from the user's Groww account. Provides detailed information on each holding "
          + "including quantity, average price, current market value, profit/loss, and other relevant metrics.")
  @Override
  public HoldingsResponse fetchHoldings() {
    try{
      return helper.fetchHoldings();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @McpTool(name = "fetch_user_positions",
      description = "Retrieves the current open positions of financial instruments (stocks, derivatives, commodities) "
          + "from the user's Groww account for a specified market segment (CASH, FNO, COMMODITY). "
          + "Provides detailed information on each position including quantity, average price, "
          + "current market value, profit/loss, and other relevant metrics.")
  @Override
  public PositionsResponse fetchUserPositions(@McpToolParam(description = "Market segment (CASH, FNO, COMMODITY)") Segment segment) {
    return helper.fetchUserPositions(segment);
  }

  @McpTool(name = "fetch_position_trading_symbol",
      description = "Retrieves the current open position of a specific financial instrument "
          + "(stock, derivative, commodity) from the user's Groww account based on the provided trading symbol "
          + "and market segment (CASH, FNO, COMMODITY). Provides detailed information on the position "
          + "including quantity, average price, current market value, profit/loss, and other relevant metrics.")
  @Override
  public PositionsResponse fetchPositionTradingSymbol(@McpToolParam(description = "Market segment (CASH, FNO, COMMODITY)")Segment segment,
      @McpToolParam(description = "Stock trading symbol") String tradingSymbol) {
    return helper.fetchPositionTradingSymbol(segment, tradingSymbol);
  }
}
