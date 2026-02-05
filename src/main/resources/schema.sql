-- Create table script for Instruments entity (PostgreSQL)

CREATE TABLE IF NOT EXISTS instruments (
    id BIGSERIAL PRIMARY KEY,
    exchange VARCHAR(50),
    exchange_token VARCHAR(100),
    trading_symbol VARCHAR(100),
    groww_symbol VARCHAR(100),
    name VARCHAR(255),
    instrument_type VARCHAR(50),
    segment VARCHAR(50),
    series VARCHAR(50),
    isin VARCHAR(50),
    underlying_symbol VARCHAR(100),
    underlying_exchange_token VARCHAR(100),
    expiry_date DATE,
    strike_price NUMERIC(15, 2),
    lot_size INTEGER,
    tick_size NUMERIC(10, 4),
    freeze_quantity INTEGER,
    is_reserved BOOLEAN,
    buy_allowed BOOLEAN,
    sell_allowed BOOLEAN,
    internal_trading_symbol VARCHAR(100),
    is_intraday BOOLEAN
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_name ON instruments(name);
CREATE INDEX idx_trading_symbol ON instruments(trading_symbol);
CREATE INDEX idx_groww_symbol ON instruments(groww_symbol);
CREATE INDEX idx_exchange ON instruments(exchange);
CREATE INDEX idx_segment ON instruments(segment);
