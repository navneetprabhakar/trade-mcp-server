# Trade MCP Server

A Spring Boot-based Model Context Protocol (MCP) server for stock trading operations, integrated with Groww API and Spring AI.

## 🚀 Features

- **MCP Server Integration**: Built with Spring AI MCP Server for seamless AI integration
- **Groww API Integration**: Fetch real-time and historical market data from Groww
- **Instruments Management**: Comprehensive CRUD operations for financial instruments
- **CSV Data Ingestion**: Bulk import instruments data from CSV files with batch processing
- **Token Management**: Automated token generation and caching for Groww API
- **Historic Data Retrieval**: Fetch candlestick data for technical analysis
- **PostgreSQL Database**: Persistent storage with JPA/Hibernate
- **Caching**: Caffeine cache implementation for improved performance

> **Note**: More API integrations (order placement, portfolio management, real-time quotes, etc.) are planned and will be updated in future releases.

## 📋 Prerequisites

- Java 21
- Maven 3.6+
- PostgreSQL 12+
- Groww API credentials (API Key and Secret)

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.10
- **AI Integration**: Spring AI 1.1.2 with MCP Server
- **Database**: PostgreSQL with Spring Data JPA
- **HTTP Client**: Apache HttpClient 5.5
- **Caching**: Caffeine Cache 3.2.3
- **CSV Processing**: Apache Commons CSV 1.11.0
- **Build Tool**: Maven
- **Java**: 21

## ⚙️ Configuration

Create the following environment variables:

```bash
# Database Configuration
export DB_URL=jdbc:postgresql://localhost:5432/trade_db
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password

# Groww API Configuration
export GROWW_API_KEY=your_groww_api_key
export GROWW_SECRET_KEY=your_groww_secret_key
```

Or configure them in `application.yaml`:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

groww:
  api-key: ${GROWW_API_KEY}
  secret: ${GROWW_SECRET_KEY}
```

## 🗄�� Database Setup

Run the schema script to create the instruments table:

```sql
psql -U your_username -d trade_db -f src/main/resources/schema.sql
```

The schema includes:
- Instruments table with 22 fields
- Indexes on frequently queried columns (name, trading_symbol, exchange, segment)

## 📦 Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd trade-mcp-server
```

2. Install dependencies:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:8082`

## 🔌 API Endpoints

### 1. Generate Token
```http
GET /v1/groww/generate-token
```
Generates a new authentication token for Groww API.

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresAt": "2026-02-05T10:30:00",
  "tokenType": "Bearer"
}
```

### 2. Ingest Instruments Data
```http
GET /v1/groww/ingest-instruments?filepath=/path/to/instruments.csv
```
Imports instruments data from a CSV file with batch processing.

**Parameters:**
- `filepath`: Path to the CSV file

**Response:**
```text
Ingestion initiated for file: /path/to/instruments.csv
```

**CSV Format:**
```csv
exchange,exchange_token,trading_symbol,groww_symbol,name,instrument_type,segment,...
NSE,123456,RELIANCE,RELIANCE,Reliance Industries Ltd,EQ,CASH,...
```

### 3. Fetch Instruments
```http
POST /v1/groww/fetch-entities
Content-Type: application/json
```

Fetches instruments matching specified criteria.

**Request Body:**
```json
{
  "name": "RELIANCE",
  "exchange": "NSE",
  "segment": "CASH"
}
```

**Response:**
```json
[
  {
    "id": 1,
    "exchange": "NSE",
    "exchangeToken": "123456",
    "tradingSymbol": "RELIANCE",
    "growwSymbol": "RELIANCE",
    "name": "Reliance Industries Ltd",
    "instrumentType": "EQ",
    "segment": "CASH",
    "lotSize": 1,
    "tickSize": 0.05,
    ...
  }
]
```

### 4. Fetch Historic Data
```http
POST /v1/groww/fetch-historic-data
Content-Type: application/json
```

Fetches historical candlestick data for an instrument.

**Request Body:**
```json
{
  "symbol": "RELIANCE",
  "exchange": "NSE",
  "interval": "1D",
  "from": "2026-01-01",
  "to": "2026-02-05"
}
```

**Response:**
```json
{
  "candles": [
    {
      "timestamp": "2026-01-01T09:15:00",
      "open": 2500.50,
      "high": 2550.00,
      "low": 2490.00,
      "close": 2545.75,
      "volume": 1500000
    }
  ]
}
```

## 📊 Domain Models

### Instruments Entity
Represents financial instruments with fields:
- Basic Info: `id`, `name`, `exchange`, `segment`
- Trading Details: `tradingSymbol`, `exchangeToken`, `growwSymbol`
- Metadata: `instrumentType`, `series`, `isin`
- Derivatives: `underlyingSymbol`, `expiryDate`, `strikePrice`
- Trading Parameters: `lotSize`, `tickSize`, `freezeQuantity`
- Permissions: `buyAllowed`, `sellAllowed`, `isIntraday`

### Enums
- **Exchange**: NSE, BSE, MCX
- **Segment**: CASH, FNO, COMMODITY
- **CandleIntervals**: 1m, 5m, 15m, 30m, 1h, 1d, 1w, 1M

## 🏗️ Architecture

```
com.navneet.trade/
├── config/                  # Configuration classes
│   └── CacheConfig.java     # Caffeine cache configuration
├── constants/               # Application constants
│   ├── CandleIntervals.java
│   ├── Exchange.java
│   ├── Segment.java
│   └── GrowwConstants.java
├── controller/              # REST controllers
│   └── GrowwController.java
├── entity/                  # JPA entities
│   ├── Instruments.java
│   ├── dto/
│   │   └── InstrumentsDto.java
│   └── repo/
│       └── InstrumentsRepo.java
├── models/                  # Request/Response models
│   ├── EntityRequest.java
│   ├── EntityResponse.java
│   ├── HistoricDataRequest.java
│   ├── HistoricDataResponse.java
│   ├── TokenRequest.java
│   └── TokenResponse.java
├── service/                 # Business logic
│   ├── GrowwService.java
│   ├── impl/
│   │   └── GrowwServiceImpl.java
│   └── helper/
│       └── GrowwServiceHelper.java
└── utils/                   # Utility classes
    ├── GrowwUtils.java
    └── RestUtils.java
```

## 🔍 Key Features Implementation

### CSV Batch Ingestion
The system uses an iterator pattern for memory-efficient CSV processing:
```java
// Reads CSV line by line
// Builds batches of configurable size (e.g., 1000 records)
// Uses instrumentsRepo.saveAll() for batch insertion
```

### Caching Strategy
- Token caching with configurable expiry
- Cache eviction support
- Caffeine cache for high performance

### Repository Queries
Custom JPA queries for flexible instrument searching:
```java
findDistinctByNameContainingIgnoreCaseAndExchangeAndSegment(
    String name, String exchange, String segment
)
```

## 🧪 Testing

Run tests with:
```bash
./mvnw test
```

## 📝 License

This project is licensed under the terms specified in the LICENSE file.

## 👤 Author

Navneet Prabhakar

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📞 Support

For issues and questions, please open an issue in the repository.

---

**Note**: This is a development server. Ensure proper security measures are in place before deploying to production.
