package com.navneet.trade.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navneet.trade.constants.GrowwConstants;
import com.navneet.trade.entity.Instruments;
import com.navneet.trade.entity.repo.InstrumentsRepo;
import com.navneet.trade.models.EntityRequest;
import com.navneet.trade.models.HistoricDataRequest;
import com.navneet.trade.models.HistoricDataResponse;
import com.navneet.trade.models.TokenRequest;
import com.navneet.trade.models.TokenResponse;
import com.navneet.trade.utils.GrowwUtils;
import com.navneet.trade.utils.RestUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author navneet.prabhakar
 */
@Service
@Slf4j
public class GrowwServiceHelper {

  @Autowired private GrowwConstants constants;
  @Autowired private GrowwUtils utils;
  @Autowired private RestUtils restUtils;
  @Autowired private CacheManager cacheManager;
  @Autowired private InstrumentsRepo instrumentsRepo;

  private static final ObjectMapper mapper=new ObjectMapper();

  public TokenResponse getTokenFromCache() throws JsonProcessingException {
    log.debug("Attempting to fetch token from cache");
    Cache cache = cacheManager.getCache("growwToken");
    if (cache != null) {
      Cache.ValueWrapper valueWrapper = cache.get("token");
      if (valueWrapper != null) {
        log.info("Token found in cache");
        return (TokenResponse) valueWrapper.get();
      }
    }
    return null;
  }

  @Cacheable(value = "growwToken", key = "'token'")
  public TokenResponse generateToken() throws JsonProcessingException {
    TokenRequest request=utils.generateTokenRequest();
    log.info("Generated TokenRequest: {}", request);
    ResponseEntity<String> response=restUtils.restPostCall(constants.getBaseUrl()+constants.getTokenUrl(), generateAuthHeaders(),null, request );
    if(response.getStatusCode().is2xxSuccessful()){
      return mapper.readValue(response.getBody(), TokenResponse.class);
    }else{
      log.error("Failed to generate token. Status Code: {}, Response Body: {}", response.getStatusCode(), response.getBody());
      return null;
    }
  }

  /**
   * Fetch historic data from Groww API based on HistoricDataRequest
   * @param request HistoricDataRequest containing exchange, segment, groww symbol, time range, and candle interval
   * @return HistoricDataResponse containing list of historical data points
   * @throws JsonProcessingException If response parsing fails
   */
  public HistoricDataResponse fetchHistoricData(HistoricDataRequest request)
      throws JsonProcessingException {
    log.info("Fetching historic data with request: {}", request);
    ResponseEntity<String> response=restUtils.restGetCall(constants.getBaseUrl()+constants.getHistoricDataUrl(),
        generateHeaders(), generateHistoricDataParams(request));
    if(response.getStatusCode().is2xxSuccessful()){
      try {
        return mapper.readValue(response.getBody(), HistoricDataResponse.class);
      } catch (JsonProcessingException e) {
        log.error("Error parsing historic data response: {}", e.getMessage());
        return null;
      }
    }else{
      log.error("Failed to fetch historic data. Status Code: {}, Response Body: {}", response.getStatusCode(), response.getBody());
      return null;
    }
  }

  /**
   * Fetch instruments from database based on EntityRequest
   * @param request EntityRequest containing name, exchange, and segment filters
   * @return List of Instruments objects
   */
  public List<Instruments> fetchInstruments(EntityRequest request) {
    log.info("Fetching instruments with request: name={}, exchange={}, segment={}",
        request.getName(), request.getExchange(), request.getSegment());

    // Fetch instruments from repository using the similar name search
    List<Instruments> instruments = instrumentsRepo.findDistinctByNameContainingIgnoreCaseAndExchangeAndSegment(
        request.getName(),
        request.getExchange().name(),
        request.getSegment().name()
    );

    log.info("Found {} instruments matching the criteria", instruments.size());


    return instruments;
  }


  /**
   * Ingest CSV file and insert instruments data in batch
   * @param csvFilePath Path to the CSV file
   * @param batchSize Number of records to insert per batch
   * @return Number of records inserted
   * @throws IOException If file reading fails
   */
  public int ingestInstrumentsFromCsv(String csvFilePath, int batchSize) throws IOException {
    log.info("Starting CSV ingestion from file: {}", csvFilePath);

    int totalRecords = 0;
    List<Instruments> instrumentsBatch = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
         CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
             .withFirstRecordAsHeader()
             .withIgnoreHeaderCase()
             .withTrim())) {

      // Iterate through CSV records
      for (CSVRecord csvRecord : csvParser) {
        Instruments instrument = mapCsvRecordToInstrument(csvRecord);
        instrumentsBatch.add(instrument);

        // Insert batch when size is reached
        if (instrumentsBatch.size() >= batchSize) {
          instrumentsRepo.saveAll(instrumentsBatch);
          totalRecords += instrumentsBatch.size();
          log.info("Inserted batch of {} records. Total so far: {}", instrumentsBatch.size(), totalRecords);
          instrumentsBatch.clear();
        }
      }

      // Insert remaining records
      if (!instrumentsBatch.isEmpty()) {
        instrumentsRepo.saveAll(instrumentsBatch);
        totalRecords += instrumentsBatch.size();
        log.debug("Inserted final batch of {} records", instrumentsBatch.size());
      }

      log.info("CSV ingestion completed. Total records inserted: {}", totalRecords);
      return totalRecords;
    } catch (IOException e) {
      log.error("Error reading CSV file: {}", csvFilePath, e);
      throw e;
    }
  }

  /**
   * Generate authentication headers for token generation API call
   * @return Map of headers for token generation API call
   */
  private Map<String,String> generateAuthHeaders(){
    return Map.of(
        "Authorization", "Bearer "+constants.getApiKey(),
        "Content-Type","application/json"
    );
  }

  /**
   * Generate headers for API calls, including the token from cache
   * @return Map of headers for API calls
   * @throws JsonProcessingException If token retrieval fails
   */
  private Map<String, String> generateHeaders() throws JsonProcessingException {
    TokenResponse token= getTokenFromCache();
    if(token==null){
      token=generateToken();
    }
    return Map.of(
        "Authorization", "Bearer "+token.getToken(),
        "Content-Type","application/json",
        "X-API-VERSION","1.0"
    );
  }

  /**
   * Generate query parameters for historic data API call based on HistoricDataRequest
   * @param request HistoricDataRequest containing exchange, segment, groww symbol, time range, and candle interval
   * @return Map of query parameters for the API call
   */
  private Map<String, String> generateHistoricDataParams(HistoricDataRequest request){
    return Map.of(
        "exchange", request.getExchange().name(),
        "segment", request.getSegment().name(),
        "groww_symbol", request.getGrowwSymbol(),
        "start_time", request.getStartTime(),
        "end_time", request.getEndTime(),
        "candle_interval", request.getCandleInterval().getInterval()
    );
  }

  /**
   * Map CSV record to Instruments entity
   * @param csvRecord CSV record
   * @return Instruments entity
   */
  private Instruments mapCsvRecordToInstrument(CSVRecord csvRecord) {
    return Instruments.builder()
        .exchange(getStringValue(csvRecord, "exchange"))
        .exchangeToken(getStringValue(csvRecord, "exchange_token"))
        .tradingSymbol(getStringValue(csvRecord, "trading_symbol"))
        .growwSymbol(getStringValue(csvRecord, "groww_symbol"))
        .name(getStringValue(csvRecord, "name"))
        .instrumentType(getStringValue(csvRecord, "instrument_type"))
        .segment(getStringValue(csvRecord, "segment"))
        .series(getStringValue(csvRecord, "series"))
        .isin(getStringValue(csvRecord, "isin"))
        .underlyingSymbol(getStringValue(csvRecord, "underlying_symbol"))
        .underlyingExchangeToken(getStringValue(csvRecord, "underlying_exchange_token"))
        .expiryDate(getStringValue(csvRecord, "expiry_date"))
        .strikePrice(getDoubleValue(csvRecord, "strike_price"))
        .lotSize(getIntegerValue(csvRecord, "lot_size"))
        .tickSize(getDoubleValue(csvRecord, "tick_size"))
        .freezeQuantity(getIntegerValue(csvRecord, "freeze_quantity"))
        .isReserved(getIntegerValue(csvRecord, "is_reserved"))
        .buyAllowed(getIntegerValue(csvRecord, "buy_allowed"))
        .sellAllowed(getIntegerValue(csvRecord, "sell_allowed"))
        .internalTradingSymbol(getStringValue(csvRecord, "internal_trading_symbol"))
        .isIntraday(getIntegerValue(csvRecord, "is_intraday"))
        .build();
  }

  /**
   * Safely get string value from CSV record
   */
  private String getStringValue(CSVRecord record, String columnName) {
    try {
      String value = record.get(columnName);
      return value != null && !value.trim().isEmpty() ? value.trim() : null;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Safely get double value from CSV record
   */
  private Double getDoubleValue(CSVRecord record, String columnName) {
    try {
      String value = record.get(columnName);
      return value != null && !value.trim().isEmpty() ? Double.parseDouble(value.trim()) : null;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Safely get integer value from CSV record
   */
  private Integer getIntegerValue(CSVRecord record, String columnName) {
    try {
      String value = record.get(columnName);
      return value != null && !value.trim().isEmpty() ? Integer.parseInt(value.trim()) : null;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }


}
