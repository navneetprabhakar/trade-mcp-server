package com.navneet.trade.controller;

import com.navneet.trade.constants.Segment;
import com.navneet.trade.entity.dto.InstrumentsDto;
import com.navneet.trade.models.request.EntityRequest;
import com.navneet.trade.models.request.HistoricDataRequest;
import com.navneet.trade.models.response.HistoricDataResponse;
import com.navneet.trade.models.response.HoldingsResponse;
import com.navneet.trade.models.response.PositionsResponse;
import com.navneet.trade.models.response.TokenResponse;
import com.navneet.trade.service.GrowwService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author navneet.prabhakar
 */
@RestController
@RequestMapping("v1/groww")
public class GrowwController {

  @Autowired private GrowwService service;


  @GetMapping("/generate-token")
  public TokenResponse generateToken() {
    return service.getToken();
  }

  @GetMapping("/ingest-instruments")
  public String ingestInstrumentsData(@RequestParam(name = "filepath") String filePath) {
    service.ingestInstrumentsData(filePath);
    return "Ingestion initiated for file: " + filePath;
  }

  @PostMapping("/fetch-entities")
  public List<InstrumentsDto> fetchEntities(@RequestBody EntityRequest request) {
    return service.fetchEntities(request);
  }

  @PostMapping("/fetch-historic-data")
  public HistoricDataResponse fetchHistoricData(@RequestBody HistoricDataRequest request) {
    return service.getHistoricData(request);
  }

  @GetMapping("/fetch-holdings")
  public HoldingsResponse fetchHoldings() {
    return service.fetchHoldings();
  }

  @GetMapping("/fetch-positions")
  public PositionsResponse fetchPositions(@RequestParam(name = "segment") Segment segment) {
    return service.fetchUserPositions(segment);
  }

  @GetMapping("/fetch-stock-positions")
  public PositionsResponse fetchStockPositions(@RequestParam(name = "segment") Segment segment,
      @RequestParam(name = "trading_symbol")String tradingSymbol) {
    return service.fetchPositionTradingSymbol(segment, tradingSymbol);
  }
}
