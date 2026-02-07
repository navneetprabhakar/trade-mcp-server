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
- **Code Optimization**: Refactored order service with centralized API handler (OrderServiceHelper)

## 📋 Prerequisites

- Java 21
- Maven 3.6+
- PostgreSQL 12+
- Groww API credentials (API Key and Secret)

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.10
- **AI Integration**: Spring AI 1.1.2 with MCP Server
- **Database**: PostgreSQL 42.7.9 with Spring Data JPA
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

## 🤖 MCP-First Architecture

This application is designed as an **MCP Server** for AI agent interactions. All trading operations are exposed as MCP tools rather than REST APIs:

- **AI-Native**: Tools are optimized for AI agents (Claude, ChatGPT, etc.)
- **Type-Safe**: All tools use strongly-typed request/response models
- **Self-Documenting**: Tool descriptions are embedded in the code using `@McpTool` annotations
- **REST APIs**: Limited to internal operations (token generation, CSV ingestion)

To interact with this server, connect it to an MCP-compatible AI client using the Spring AI MCP protocol.

## 🛠️ MCP Tools

This server exposes the following MCP tools for AI agents to interact with trading operations:

### Market Data Tools

#### 1. `fetch_historic_data`
Retrieves historical candlestick data (OHLCV - Open, High, Low, Close, Volume) for technical analysis and charting.

**Parameters:**
- `request` (HistoricDataRequest): Contains symbol, exchange, interval, from and to dates

**Supported Intervals:** 1m, 5m, 15m, 30m, 1h, 1d, 1w, 1M

**Example:**
```json
{
  "symbol": "RELIANCE",
  "exchange": "NSE",
  "interval": "1D",
  "from": "2026-01-01",
  "to": "2026-02-05"
}
```

#### 2. `fetch_entities`
Searches and retrieves financial instruments from the database based on partial name matching, exchange, and segment.

**Parameters:**
- `request` (EntityRequest): Contains name, exchange (NSE, BSE, MCX), and segment (CASH, FNO, COMMODITY)

**Returns:** List of instruments with trading symbols, lot sizes, tick sizes, and trading permissions

**Example:**
```json
{
  "name": "RELIANCE",
  "exchange": "NSE",
  "segment": "CASH"
}
```

### Portfolio Tools

#### 3. `fetch_holdings`
Retrieves all holdings from the user's Groww portfolio with detailed quantity and price information.

**Parameters:** None

**Returns:** Holdings with quantity, average price, locked quantities, and free quantities

#### 4. `fetch_user_positions`
Retrieves current open positions for a specified market segment.

**Parameters:**
- `segment` (Segment): Market segment - CASH, FNO, or COMMODITY

**Returns:** Positions with credit/debit quantities, prices, and realized P&L

#### 5. `fetch_position_trading_symbol`
Retrieves position for a specific trading symbol within a segment.

**Parameters:**
- `segment` (Segment): Market segment - CASH, FNO, or COMMODITY
- `tradingSymbol` (String): Stock trading symbol (e.g., "TCS", "RELIANCE")

**Returns:** Position details with quantity, average price, and P&L

### Order Management Tools

#### 6. `create_new_order`
Creates a new buy or sell order with support for multiple order types.

**Parameters:**
- `request` (CreateOrderRequest): Order details including:
  - `tradingSymbol`: Stock symbol
  - `quantity`: Number of shares
  - `price`: Order price
  - `triggerPrice`: Stop loss trigger price (for SL orders)
  - `validity`: DAY or IOC
  - `exchange`: NSE, BSE, or MCX
  - `segment`: CASH, FNO, or COMMODITY
  - `product`: CNC, INTRADAY, or MTF
  - `orderType`: MARKET, LIMIT, SL, or SL-M
  - `transactionType`: BUY or SELL
  - `orderReferenceId`: Unique client reference

**Example:**
```json
{
  "tradingSymbol": "WIPRO",
  "quantity": 100,
  "price": 2500,
  "triggerPrice": 2450,
  "validity": "DAY",
  "exchange": "NSE",
  "segment": "CASH",
  "product": "CNC",
  "orderType": "SL",
  "transactionType": "BUY",
  "orderReferenceId": "Ab-654321234-1628190"
}
```

#### 7. `modify_order`
Modifies an existing order's price and/or quantity.

**Parameters:**
- `request` (ModifyOrderRequest): Contains order ID and new parameters (price, quantity, validity)

**Example:**
```json
{
  "growwOrderId": "GMK39038RDT490CCVRO",
  "price": 2550,
  "quantity": 150,
  "validity": "DAY"
}
```

#### 8. `cancel_order`
Cancels an existing open order.

**Parameters:**
- `request` (CancelOrderRequest): Contains order ID to cancel

**Example:**
```json
{
  "growwOrderId": "GMK39038RDT490CCVRO"
}
```

### Order Tracking Tools

#### 9. `fetch_order_status`
Fetches the current status of a specific order.

**Parameters:**
- `request` (OrderStatusRequest): Contains order ID and segment

**Returns:** Order status, filled quantity, and order reference

#### 10. `fetch_trades_for_order`
Fetches all trades associated with a specific order.

**Parameters:**
- `request` (OrderTradesRequest): Contains order ID, segment, page number, and page size

**Returns:** List of executed trades with price, quantity, timestamps, and settlement details

#### 11. `fetch_order_list`
Fetches the list of all orders for a specific segment.

**Parameters:**
- `segment` (Segment): CASH, FNO, or COMMODITY

**Returns:** List of orders with complete order details, execution status, and metadata

#### 12. `fetch_order_details`
Fetches detailed information for a specific order.

**Parameters:**
- `request` (OrderStatusRequest): Contains order ID and segment

**Returns:** Complete order details including execution information and timestamps

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
├── controller/              # REST controllers (internal use only)
│   ├── GrowwController.java  # Token generation & CSV ingestion
│   └── OrderController.java  # Disabled - testing purposes only
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
│   │   └── OrderTradesRequest.java
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
│       └── OrderServiceHelper.java
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

### Code Optimization: OrderServiceHelper
Reduced code duplication in order management operations:
- **Centralized API Handling**: OrderServiceHelper consolidates all REST API call logic (POST and GET)
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
