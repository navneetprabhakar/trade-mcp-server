package com.navneet.trade.service.impl;

import com.navneet.trade.constants.GrowwConstants;
import com.navneet.trade.constants.Segment;
import com.navneet.trade.models.request.CancelOrderRequest;
import com.navneet.trade.models.request.CreateOrderRequest;
import com.navneet.trade.models.request.ModifyOrderRequest;
import com.navneet.trade.models.request.OrderStatusRequest;
import com.navneet.trade.models.request.OrderTradesRequest;
import com.navneet.trade.models.response.CreateOrderResponse;
import com.navneet.trade.models.response.ModifyOrderResponse;
import com.navneet.trade.models.response.OrderListResponse;
import com.navneet.trade.models.response.OrderStatusResponse;
import com.navneet.trade.models.response.OrderTradesResponse;
import com.navneet.trade.service.OrderService;
import com.navneet.trade.service.helper.OrderServiceHelper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author navneet.prabhakar
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

  @Autowired private GrowwConstants constants;
  @Autowired private OrderServiceHelper orderServiceHelper;


  @McpTool(name = "create_new_order", description = "Creates a new buy or sell order based on the provided request.")
  @Override
  public CreateOrderResponse createNewOrder(
      @McpToolParam(description = "The request containing order details such as trading symbol, quantity, price, order type, and product type.") CreateOrderRequest request) {
    return orderServiceHelper.executePostCall("Creating new order with request: {}", constants.getCreateOrderUrl(), request, CreateOrderResponse.class);
  }

  @McpTool(name = "modify_order", description = "Modifies an existing order based on the provided request.")
  @Override
  public ModifyOrderResponse modifyOrder(
      @McpToolParam(description = "The request containing the order ID to modify and new order parameters such as price and quantity.") ModifyOrderRequest request) {
    return orderServiceHelper.executePostCall("Modifying order with request: {}", constants.getModifyOrderUrl(), request, ModifyOrderResponse.class);
  }

  @McpTool(name = "cancel_order", description = "Cancels an existing order based on the provided request.")
  @Override
  public ModifyOrderResponse cancelOrder(
      @McpToolParam(description = "The request containing the order ID to be cancelled.") CancelOrderRequest request) {
    return orderServiceHelper.executePostCall("Cancelling order with request: {}", constants.getCancelOrderUrl(), request, ModifyOrderResponse.class);
  }

  @McpTool(name = "fetch_trades_for_order", description = "Fetches the trades associated with a specific order based on the provided request.")
  @Override
  public OrderTradesResponse fetchTradesForOrder(
      @McpToolParam(description = "The request containing the order ID to fetch associated trades for.") OrderTradesRequest request) {
    Map<String, String> params = Map.of(
        "segment", request.getSegment().name(),
        "page", String.valueOf(request.getPageNumber()),
        "page_size", String.valueOf(request.getPageSize())
    );
    return orderServiceHelper.executeGetCall("Fetching trades for order with request: {}", constants.getOrderTradesUrl() + request.getOrderId(), params, OrderTradesResponse.class);
  }

  @McpTool(name = "fetch_order_status", description = "Fetches the current status of a specific order based on the provided request.")
  @Override
  public OrderStatusResponse fetchOrderStatus(
      @McpToolParam(description = "The request containing the order ID to fetch the status for.") OrderStatusRequest request) {
    Map<String, String> params = Map.of("segment", request.getSegment().name());
    return orderServiceHelper.executeGetCall("Fetching order status for order with request: {}", constants.getOrderStatusUrl() + request.getGrowwOrderId(), params, OrderStatusResponse.class);
  }

  @McpTool(name = "fetch_order_list", description = "Fetches the list of orders for a specific segment.")
  @Override
  public OrderListResponse fetchOrderList(
      @McpToolParam(description = "Segment name supported CASH, FNO, COMMODITY") Segment segment) {
    Map<String, String> params = Map.of(
        "segment", segment.name(),
        "page", "0",
        "page_size", "100"
    );
    return orderServiceHelper.executeGetCall("Fetching order list for segment: {}", constants.getOrderListUrl(), params, OrderListResponse.class);
  }

  @McpTool(name = "fetch_order_details", description = "Fetches the details of a specific order based on the provided request.")
  @Override
  public OrderListResponse fetchOrderDetails(
      @McpToolParam(description = "The request containing the order ID to fetch the status for.") OrderStatusRequest request) {
    Map<String, String> params = Map.of("segment", request.getSegment().name());
    return orderServiceHelper.executeGetCall("Fetching order details for request: {}", constants.getOrderDetailsUrl() + request.getGrowwOrderId(), params, OrderListResponse.class);
  }
}
