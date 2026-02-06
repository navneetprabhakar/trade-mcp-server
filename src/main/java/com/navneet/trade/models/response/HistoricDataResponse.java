package com.navneet.trade.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Data;

import java.io.IOException;
import java.util.List;

/**
 * @author navneet.prabhakar
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoricDataResponse {
    private String status;
    private Payload payload;

    /**
     * Converts JSON string to HistoricDataResponse object
     * @param json JSON string
     * @return HistoricDataResponse object
     * @throws JsonProcessingException if JSON parsing fails
     */
    public static HistoricDataResponse fromJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Candle.class, new CandleDeserializer());
        mapper.registerModule(module);
        return mapper.readValue(json, HistoricDataResponse.class);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        @JsonDeserialize(contentUsing = CandleDeserializer.class)
        private List<Candle> candles;

        @JsonProperty("closing_price")
        private Double closingPrice;

        @JsonProperty("start_time")
        private String startTime;

        @JsonProperty("end_time")
        private String endTime;

        @JsonProperty("interval_in_minutes")
        private Integer intervalInMinutes;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candle {
        private String timestamp;
        private Double open;
        private Double high;
        private Double low;
        private Double close;
        private Long volume;
        private Long openInterest;

        public Candle() {
        }

        public Candle(List<Object> candleData) {
            if (candleData != null && candleData.size() >= 6) {
                this.timestamp = (String) candleData.get(0);
                this.open = ((Number) candleData.get(1)).doubleValue();
                this.high = ((Number) candleData.get(2)).doubleValue();
                this.low = ((Number) candleData.get(3)).doubleValue();
                this.close = ((Number) candleData.get(4)).doubleValue();
                this.volume = ((Number) candleData.get(5)).longValue();
                this.openInterest = candleData.size() > 6 && candleData.get(6) != null
                    ? ((Number) candleData.get(6)).longValue()
                    : null;
            }
        }
    }

    /**
     * Custom deserializer for Candle class to handle array format from JSON
     */
    public static class CandleDeserializer extends JsonDeserializer<Candle> {
        @Override
        public Candle deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            List<Object> candleData = parser.readValueAs(List.class);
            return new Candle(candleData);
        }
    }
}
