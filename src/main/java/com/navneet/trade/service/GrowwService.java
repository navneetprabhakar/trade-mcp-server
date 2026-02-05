package com.navneet.trade.service;

import com.navneet.trade.entity.dto.InstrumentsDto;
import com.navneet.trade.models.EntityRequest;
import com.navneet.trade.models.HistoricDataRequest;
import com.navneet.trade.models.HistoricDataResponse;
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
}
