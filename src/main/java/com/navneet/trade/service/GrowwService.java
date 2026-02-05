package com.navneet.trade.service;

import com.navneet.trade.constants.Segment;
import com.navneet.trade.entity.dto.InstrumentsDto;
import com.navneet.trade.models.EntityRequest;
import com.navneet.trade.models.HistoricDataRequest;
import com.navneet.trade.models.HistoricDataResponse;
import com.navneet.trade.models.HoldingsResponse;
import com.navneet.trade.models.PositionsResponse;
import com.navneet.trade.models.TokenResponse;
import java.util.List;

/**
 * @author navneet.prabhakar
 */
public interface GrowwService {
  /**
   * Generates a new token by making an API call to Groww.
   * This method is intended to be used when the token is not present in the cache or has expired.
   *
   * @return A new TokenResponse containing the generated token and its metadata.
   */
  TokenResponse getToken();

  /**
   * Fetches historic data based on the provided HistoricDataRequest.
   * This method makes an API call to Groww to retrieve the historic data for the specified instrument and time range.
   *
   * @param request The HistoricDataRequest containing the criteria for fetching historic data.
   * @return A HistoricDataResponse containing the retrieved historic data matching the request criteria.
   */
  HistoricDataResponse getHistoricData(HistoricDataRequest request);

  /**
   * Ingests instruments data from a specified CSV file path.
   * This method reads the CSV file, processes the data, and stores it in the database.
   *
   * @param filePath The path to the CSV file containing the instruments data to be ingested.
   */
  void ingestInstrumentsData(String filePath);

  /**
   * Fetches entities based on the provided EntityRequest.
   *
   * @param request The EntityRequest containing the criteria for fetching entities.
   * @return A list of EntityResponse objects matching the request criteria.
   */
  List<InstrumentsDto> fetchEntities(EntityRequest request);

  /**
   * Fetches the current holdings from Groww.
   *
   * @return A HoldingsResponse containing the current holdings data.
   */
  HoldingsResponse fetchHoldings();

  /**
   * Fetches the current positions for a given segment from Groww.
   *
   * @param segment The segment for which to fetch positions (e.g., EQUITY, DERIVATIVES).
   * @return A PositionsResponse containing the current positions data for the specified segment.
   */
  PositionsResponse fetchUserPositions(Segment segment);

  /**
   * Fetches the position for a specific trading symbol within a given segment from Groww.
   *
   * @param segment The segment for which to fetch the position (e.g., EQUITY, DERIVATIVES).
   * @param tradingSymbol The trading symbol for which to fetch the position.
   * @return A PositionsResponse containing the position data for the specified trading symbol.
   */
  PositionsResponse fetchPositionTradingSymbol(Segment segment, String tradingSymbol);
}
