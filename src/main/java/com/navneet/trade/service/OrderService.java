package com.navneet.trade.service;

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

/**
 * @author navneet.prabhakar
 */
public interface OrderService {

  /**
   * Creates a new buy or sell order based on the provided CreateOrderRequest.
   *
   * @param request The CreateOrderRequest containing the details of the order to be created.
   * @return A CreateOrderResponse containing the details of the created order, including its status and any relevant metadata.
   */
  CreateOrderResponse createNewOrder(CreateOrderRequest request);

  /**
   * Modifies an existing order based on the provided ModifyOrderRequest.
   *
   * @param request The ModifyOrderRequest containing the details of the order to be modified.
   * @return A ModifyOrderResponse containing the details of the modified order, including its status and any relevant metadata.
   */
  ModifyOrderResponse modifyOrder(ModifyOrderRequest request);

  /**
   * Cancels an existing order based on the provided CancelOrderRequest.
   *
   * @param request The CancelOrderRequest containing the details of the order to be cancelled.
   * @return A ModifyOrderResponse containing the details of the cancelled order, including its status and any relevant metadata.
   */
  ModifyOrderResponse cancelOrder(CancelOrderRequest request);

  /**
   * Fetches the trades associated with a specific order based on the provided OrderTradesRequest.
   *
   * @param request The OrderTradesRequest containing the details of the order for which trades are to be fetched.
   * @return An OrderTradesResponse containing the list of trades associated with the specified order, including their details and any relevant metadata.
   */
  OrderTradesResponse fetchTradesForOrder(OrderTradesRequest request);

  /**
   * Fetches the current status of a specific order based on the provided OrderStatusRequest.
   *
   * @param request The OrderStatusRequest containing the details of the order for which the status is to be fetched.
   * @return An OrderStatusResponse containing the current status of the specified order, including any relevant metadata.
   */
  OrderStatusResponse fetchOrderStatus(OrderStatusRequest request);

  /**
   * Fetches the list of orders for a specific segment.
   *
   * @param segment The trading segment for which the order list is to be fetched.
   * @return An OrderListResponse containing the list of orders for the specified segment, including their details and any relevant metadata.
   * Note: The value of page and page_size is fixed (0, 100) , since this is for single person use, we can change the limits as per requirement in future.
   */
  OrderListResponse fetchOrderList(Segment segment);

  /**
   * Fetches the details of a specific order based on the provided OrderStatusRequest.
   *
   * @param request The OrderStatusRequest containing the details of the order for which the details are to be fetched.
   * @return An OrderListResponse containing the details of the specified order, including its status and any relevant metadata.
   */
  OrderListResponse fetchOrderDetails(OrderStatusRequest request);
}
