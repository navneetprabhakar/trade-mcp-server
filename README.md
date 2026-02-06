# Trade MCP Server

A Spring Boot-based Model Context Protocol (MCP) server for stock trading operations, integrated with Groww API and Spring AI.

## 🚀 Features

- **MCP Server Integration**: Built with Spring AI MCP Server for seamless AI integration
- **Groww API Integration**: Fetch real-time and historical market data from Groww
- **Instruments Management**: Comprehensive CRUD operations for financial instruments
- **CSV Data Ingestion**: Bulk import instruments data from CSV files with batch processing
- **Token Management**: Automated token generation and caching for Groww API
- **Historic Data Retrieval**: Fetch candlestick data for technical analysis
- **Holdings Management**: Fetch and monitor user holdings with detailed position information
- **Positions Tracking**: Real-time position tracking by segment and trading symbol
- **Order Placement**: Place buy/sell orders with support for multiple order types (Market, Limit, SL, SL-M)
- **Order Management**: Modify, cancel, and track orders with real-time status updates
- **Trade Execution**: Fetch executed trades and detailed trade information
- **Order History**: Retrieve comprehensive order list and order details
- **PostgreSQL Database**: Persistent storage with JPA/Hibernate
- **Caching**: Caffeine cache implementation for improved performance
- **Code Optimization**: Refactored order service with centralized API handler (OrderApiHelper)

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

### 5. Fetch Holdings
```http
GET /v1/groww/fetch-holdings
```

Fetches all holdings from the user's portfolio.

**Response:**
```json
{
  "status": "success",
  "payload": {
    "holdings": [
      {
        "isin": "INE002A01018",
        "tradingSymbol": "RELIANCE",
        "quantity": 50,
        "averagePrice": 2450.50,
        "pledgeQuantity": 0,
        "dematLockedQuantity": 0,
        "growwLockedQuantity": 0.0,
        "repledgeQuantity": 0.0,
        "t1Quantity": 0,
        "dematFreeQuantity": 50,
        "corporateActionAdditionalQuantity": 0,
        "activeDematTransferQuantity": 0
      }
    ]
  }
}
```

### 6. Fetch Positions
```http
GET /v1/groww/fetch-positions?segment=CASH
```

Fetches all open positions for a specific segment.

**Parameters:**
- `segment`: Trading segment (CASH, FNO, COMMODITY)

**Response:**
```json
{
  "status": "success",
  "payload": {
    "positions": [
      {
        "tradingSymbol": "TCS",
        "creditQuantity": 10,
        "creditPrice": 3500.50,
        "debitQuantity": 0,
        "debitPrice": 0.0,
        "carryForwardCreditQuantity": 0,
        "carryForwardCreditPrice": 0.0,
        "carryForwardDebitQuantity": 0,
        "carryForwardDebitPrice": 0.0,
        "exchange": "NSE",
        "symbolIsin": "INE467B01029",
        "quantity": 10,
        "product": "INTRADAY",
        "netCarryForwardQuantity": 0,
        "netPrice": 3500.50,
        "netCarryForwardPrice": 0.0,
        "realisedPnl": 0.0
      }
    ]
  }
}
```

### 7. Fetch Stock Positions
```http
GET /v1/groww/fetch-stock-positions?segment=CASH&trading_symbol=TCS
```

Fetches position for a specific trading symbol within a segment.

**Parameters:**
- `segment`: Trading segment (CASH, FNO, COMMODITY)
- `trading_symbol`: The trading symbol (e.g., TCS, RELIANCE)

**Response:**
```json
{
  "status": "success",
  "payload": {
    "positions": [
      {
        "tradingSymbol": "TCS",
        "creditQuantity": 10,
        "creditPrice": 3500.50,
        "debitQuantity": 0,
        "debitPrice": 0.0,
        "exchange": "NSE",
        "symbolIsin": "INE467B01029",
        "quantity": 10,
        "product": "INTRADAY",
        "netPrice": 3500.50,
        "realisedPnl": 0.0
      }
    ]
  }
}
```

### 8. Create Order
```http
POST /v1/orders/create
Content-Type: application/json
```

Place a new buy or sell order with various order types.

**Request Body:**
```json
{
  "trading_symbol": "WIPRO",
  "quantity": 100,
  "price": 2500,
  "trigger_price": 2450,
  "validity": "DAY",
  "exchange": "NSE",
  "segment": "CASH",
  "product": "CNC",
  "order_type": "SL",
  "transaction_type": "BUY",
  "order_reference_id": "Ab-654321234-1628190"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "payload": {
    "groww_order_id": "GMK39038RDT490CCVRO",
    "order_status": "OPEN",
    "order_reference_id": "Ab-654321234-1628190",
    "remark": "Order placed successfully"
  }
}
```

### 9. Modify Order
```http
POST /v1/orders/modify
Content-Type: application/json
```

Modifies an existing order's price and/or quantity.

**Request Body:**
```json
{
  "groww_order_id": "GMK39038RDT490CCVRO",
  "price": 2550,
  "quantity": 150,
  "validity": "DAY"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "payload": {
    "groww_order_id": "GMK39038RDT490CCVRO",
    "order_status": "OPEN",
    "remark": "Order modified successfully"
  }
}
```

### 10. Cancel Order
```http
POST /v1/orders/cancel
Content-Type: application/json
```

Cancels an existing open order.

**Request Body:**
```json
{
  "groww_order_id": "GMK39038RDT490CCVRO"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "payload": {
    "groww_order_id": "GMK39038RDT490CCVRO",
    "order_status": "CANCELLED",
    "remark": "Order cancelled successfully"
  }
}
```

### 11. Fetch Order Status
```http
GET /v1/orders/status/{groww_order_id}?segment=CASH
```

Fetches the current status of a specific order.

**Parameters:**
- `segment`: Trading segment (CASH, FNO, COMMODITY)

**Response:**
```json
{
  "status": "SUCCESS",
  "payload": {
    "groww_order_id": "GMK39038RDT490CCVRO",
    "order_status": "OPEN",
    "remark": "Order placed successfully",
    "filled_quantity": 100,
    "order_reference_id": "Ab-654321234-1628190"
  }
}
```

### 12. Fetch Trades for Order
```http
GET /v1/orders/trades/{order_id}?segment=CASH&page=0&page_size=10
```

Fetches all trades associated with a specific order.

**Parameters:**
- `segment`: Trading segment (CASH, FNO, COMMODITY)
- `page`: Page number for pagination (default: 0)
- `page_size`: Number of records per page (default: 10)

**Response:**
```json
{
  "status": "SUCCESS",
  "payload": {
    "trade_list": [
      {
        "price": 2500,
        "isin": "ISN378331005",
        "quantity": 100,
        "groww_order_id": "GROW123456",
        "groww_trade_id": "TRADE123456",
        "exchange_trade_id": "EXCH123456",
        "exchange_order_id": "EXORD123456",
        "trade_status": "COMPLETED",
        "trading_symbol": "AAPL",
        "remark": "Trade executed successfully",
        "exchange": "NSE",
        "segment": "CASH",
        "product": "CNC",
        "transaction_type": "BUY",
        "created_at": "2024-08-24T14:15:22Z",
        "trade_date_time": "2024-08-24T14:15:22Z",
        "settlement_number": "SETTLE123456"
      }
    ]
  }
}
```

### 13. Fetch Order List
```http
GET /v1/orders/list?segment=CASH&page=0&page_size=100
```

Fetches the list of orders for a specific segment.

**Parameters:**
- `segment`: Trading segment (CASH, FNO, COMMODITY)
- `page`: Page number for pagination (default: 0)
- `page_size`: Number of records per page (default: 100)

**Response:**
```json
{
  "status": "SUCCESS",
  "payload": {
    "order_list": [
      {
        "groww_order_id": "GMK39038RDT490CCVRO",
        "trading_symbol": "WIPRO",
        "order_status": "OPEN",
        "remark": "Order placed successfully",
        "quantity": 100,
        "price": 2500,
        "trigger_price": 2450,
        "filled_quantity": 100,
        "remaining_quantity": 10,
        "average_fill_price": 2500,
        "deliverable_quantity": 10,
        "amo_status": "PENDING",
        "validity": "DAY",
        "exchange": "NSE",
        "order_type": "MARKET",
        "transaction_type": "BUY",
        "segment": "CASH",
        "product": "CNC",
        "created_at": "2023-10-01T10:15:30",
        "exchange_time": "2023-10-01T10:15:30",
        "trade_date": "2019-08-24T14:15:22Z",
        "order_reference_id": "Ab-654321234-1628190"
      }
    ]
  }
}
```

### 14. Fetch Order Details
```http
GET /v1/orders/details/{groww_order_id}?segment=CASH
```

Fetches detailed information for a specific order.

**Parameters:**
- `segment`: Trading segment (CASH, FNO, COMMODITY)

**Response:**
```json
{
  "status": "SUCCESS",
  "payload": {
    "order_list": [
      {
        "groww_order_id": "GMK39038RDT490CCVRO",
        "trading_symbol": "WIPRO",
        "order_status": "OPEN",
        "remark": "Order placed successfully",
        "quantity": 100,
        "price": 2500,
        "trigger_price": 2450,
        "filled_quantity": 100,
        "remaining_quantity": 10,
        "average_fill_price": 2500,
        "deliverable_quantity": 10,
        "amo_status": "PENDING",
        "validity": "DAY",
        "exchange": "NSE",
        "order_type": "MARKET",
        "transaction_type": "BUY",
        "segment": "CASH",
        "product": "CNC",
        "created_at": "2023-10-01T10:15:30",
        "exchange_time": "2023-10-01T10:15:30",
        "trade_date": "2019-08-24T14:15:22Z",
        "order_reference_id": "Ab-654321234-1628190"
      }
    ]
  }
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

### Holdings Response
Represents user holdings with detailed information:
- **Holding**: `isin`, `tradingSymbol`, `quantity`, `averagePrice`
- **Lock Details**: `pledgeQuantity`, `dematLockedQuantity`, `growwLockedQuantity`, `repledgeQuantity`
- **Quantity Types**: `t1Quantity`, `dematFreeQuantity`, `corporateActionAdditionalQuantity`, `activeDematTransferQuantity`

### Positions Response
Represents user positions with comprehensive position tracking:
- **Position Details**: `tradingSymbol`, `exchange`, `symbolIsin`, `product`
- **Credit Info**: `creditQuantity`, `creditPrice`, `carryForwardCreditQuantity`, `carryForwardCreditPrice`
- **Debit Info**: `debitQuantity`, `debitPrice`, `carryForwardDebitQuantity`, `carryForwardDebitPrice`
- **Net Values**: `quantity`, `netPrice`, `netCarryForwardQuantity`, `netCarryForwardPrice`, `realisedPnl`

### Create Order Request
Represents order placement request with trading parameters:
- **Instrument Details**: `tradingSymbol`, `exchange`, `segment`
- **Order Parameters**: `quantity`, `price`, `triggerPrice`, `validity`
- **Order Type**: `orderType` (MARKET, LIMIT, SL, SL-M), `transactionType` (BUY, SELL)
- **Product Type**: `product` (CNC, INTRADAY, MTF)
- **Reference**: `orderReferenceId` (unique order reference)

### Create Order Response
Represents order placement response with order status:
- **Status**: `status` (SUCCESS, FAILED)
- **Payload**: 
  - `growwOrderId`: Unique order ID from Groww
  - `orderStatus`: Current order status (OPEN, PENDING, EXECUTED, CANCELLED, REJECTED)
  - `orderReferenceId`: Client-provided order reference
  - `remark`: Additional information about the order

### Modify Order Response
Represents order modification/cancellation response:
- **Status**: `status` (SUCCESS, FAILED)
- **Payload**:
  - `growwOrderId`: Unique order ID from Groww
  - `orderStatus`: Updated order status
  - `remark`: Modification confirmation message

### Order Status Response
Represents current order status with execution details:
- **Status**: `status` (SUCCESS, FAILED)
- **Payload**:
  - `growwOrderId`: Unique order ID from Groww
  - `orderStatus`: Current order status
  - `filledQuantity`: Quantity executed so far
  - `orderReferenceId`: Client-provided order reference
  - `remark`: Status message

### Order Trades Response
Represents list of trades executed for an order:
- **Status**: `status` (SUCCESS, FAILED)
- **Payload**:
  - `tradeList`: Array of executed trades containing:
    - **Trade Details**: `price`, `quantity`, `isin`, `tradingSymbol`
    - **Order References**: `growwOrderId`, `exchangeOrderId`, `orderReferenceId`
    - **Trade References**: `growwTradeId`, `exchangeTradeId`, `settlementNumber`
    - **Status & Type**: `tradeStatus`, `transactionType` (BUY, SELL)
    - **Market Info**: `exchange`, `segment`, `product`
    - **Timestamps**: `createdAt`, `tradeDatetime`
    - **Metadata**: `remark`

### Order List Response
Represents list of orders for a segment:
- **Status**: `status` (SUCCESS, FAILED)
- **Payload**:
  - `orderList`: Array of orders containing:
    - **Order Identification**: `growwOrderId`, `tradingSymbol`, `orderReferenceId`
    - **Order Status**: `orderStatus`, `amoStatus`
    - **Order Parameters**: `quantity`, `price`, `triggerPrice`, `validity`
    - **Execution Details**: `filledQuantity`, `remainingQuantity`, `averageFillPrice`
    - **Position Info**: `deliverableQuantity`
    - **Order Type**: `orderType` (MARKET, LIMIT, SL, SL-M), `transactionType` (BUY, SELL)
    - **Market Info**: `exchange`, `segment`, `product`
    - **Timestamps**: `createdAt`, `exchangeTime`, `tradeDate`
    - **Metadata**: `remark`

### Enums
- **Exchange**: NSE, BSE, MCX
- **Segment**: CASH, FNO, COMMODITY
- **CandleIntervals**: 1m, 5m, 15m, 30m, 1h, 1d, 1w, 1M
- **OrderType**: MARKET, LIMIT, SL (Stop Loss), SL-M (Stop Loss Market)
- **OrderStatus**: OPEN, PENDING, EXECUTED, CANCELLED, REJECTED
- **ProductType**: CNC (Cash & Carry), INTRADAY, MTF (Margin Trading Facility)
- **TransactionType**: BUY, SELL

## 🏗️ Architecture

```
com.navneet.trade/
├── config/                  # Configuration classes
│   └── CacheConfig.java     # Caffeine cache configuration
├── constants/               # Application constants
│   ├── CandleIntervals.java
│   ├── Exchange.java
│   ├── Segment.java
│   ├── OrderType.java
│   ├── OrderStatus.java
│   ├── ProductType.java
│   ├── TransactionType.java
│   └── GrowwConstants.java
├── controller/              # REST controllers
│   ├── GrowwController.java
│   └── OrderController.java
├── entity/                  # JPA entities
│   ├── Instruments.java
│   ├── dto/
│   │   └── InstrumentsDto.java
│   └── repo/
│       └── InstrumentsRepo.java
├── models/                  # Request/Response models
│   ├── request/
│   │   ├── EntityRequest.java
│   │   ├── HistoricDataRequest.java
│   │   ├── TokenRequest.java
│   │   ├── CreateOrderRequest.java
│   │   ├── ModifyOrderRequest.java
│   │   ├── CancelOrderRequest.java
│   │   ├── OrderStatusRequest.java
│   │   ├── OrderTradesRequest.java
│   │   └── OrderListRequest.java
│   └── response/
│       ├── TokenResponse.java
│       ├── HistoricDataResponse.java
│       ├── HoldingsResponse.java
│       ├── PositionsResponse.java
│       ├── CreateOrderResponse.java
│       ├── ModifyOrderResponse.java
│       ├── OrderStatusResponse.java
│       ├── OrderTradesResponse.java
│       └── OrderListResponse.java
├── service/                 # Business logic
│   ├── GrowwService.java
│   ├── OrderService.java
│   ├── impl/
│   │   ├── GrowwServiceImpl.java
│   │   └── OrderServiceImpl.java
│   └── helper/
│       ├── GrowwServiceHelper.java
│       └── OrderApiHelper.java
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

### Code Optimization: OrderApiHelper
Reduced code duplication in order management operations:
- **Centralized API Handling**: OrderApiHelper consolidates all REST API call logic (POST and GET)
- **Generic Methods**: `executePostCall()` and `executeGetCall()` handle all API interactions
- **Response Handling**: Unified JSON deserialization and error handling
- **Result**: 50% reduction in OrderServiceImpl code (192 → 96 lines)
- **Benefit**: Bug fixes and enhancements in API communication now happen in one place

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
