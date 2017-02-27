package com.sbm.orders.liveorderboard.display;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.sbm.orders.liveorderboard.domain.OrderQuantity;
import com.sbm.orders.liveorderboard.domain.OrderType;
import com.sbm.orders.liveorderboard.domain.OrderUnit;
import com.sbm.orders.liveorderboard.domain.Price;
import com.sbm.orders.liveorderboard.domain.OrderSummary;
import com.sbm.orders.liveorderboard.domain.UnitPrice;
import com.sbm.orders.liveorderboard.exceptions.OrderNotFoundException;
import com.sbm.orders.liveorderboard.service.OrderService;
import com.sbm.orders.liveorderboard.service.OrderServiceImpl;
import com.sbm.orders.liveorderboard.service.OrderSummaryService;
import com.sbm.orders.liveorderboard.service.OrderSummaryServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class LiveOrderBoardTest {

    private LiveOrderBoard liveOrderBoard;

    @Before
    public void setUp() throws Exception {
        liveOrderBoard = new LiveOrderBoard();
    }

    @Test
    public void shouldGetSingleOrderSummary() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(1.5), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(307), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary summaryOrder = new OrderSummary(orderQuantity, unitPrice, OrderType.SELL);

        // When
        List<String> placedOrders = liveOrderBoard.renderSummaryOrderInformation(Lists.newArrayList(summaryOrder));

        // Then
        assertThat(placedOrders.get(0), is("1.50 kg for £307"));
    }

    @Test
    public void shouldGetMultipleOrdersSummary() throws Exception {
        // Given
        OrderQuantity orderQuantity1 = new OrderQuantity(new BigDecimal(1.5), OrderUnit.KILO);
        UnitPrice unitPrice1 = new UnitPrice(new Price(new BigDecimal(307), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary1 = new OrderSummary(orderQuantity1,
                        unitPrice1, OrderType.SELL);

        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(310), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary2 = new OrderSummary(orderQuantity2, unitPrice2, OrderType.SELL);

        // When
        List<String> renderedOrders = liveOrderBoard.renderSummaryOrderInformation(Lists.newArrayList(orderSummary1, orderSummary2));

        // Then
        assertThat(renderedOrders.get(0), is("1.50 kg for £307"));
        assertThat(renderedOrders.get(1), is("2.50 kg for £310"));
    }

    @Test
    public void shouldRenderTheRegisteredOrders() throws Exception {
        // Given
        OrderService orderService = new OrderServiceImpl();
        OrderSummaryService orderSummaryService = new OrderSummaryServiceImpl();

        placeOrder("user1", 3.5, 306, OrderType.SELL, orderService);
        placeOrder("user2", 1.2, 310, OrderType.SELL, orderService);
        placeOrder("user3", 1.5, 307, OrderType.SELL, orderService);
        placeOrder("user4", 2.0, 306, OrderType.SELL, orderService);

        List<OrderSummary> summaryOrders = orderSummaryService.getSummaryOrders(orderService.getLiveOrders());

        // When
        List<String> renderedOrders = liveOrderBoard.renderSummaryOrderInformation(summaryOrders);

        //Then
        assertThat(renderedOrders.get(0), is("5.50 kg for £306"));
        assertThat(renderedOrders.get(1), is("1.50 kg for £307"));
        assertThat(renderedOrders.get(2), is("1.20 kg for £310"));
    }

    @Test
    public void shouldRemoveCancelledOrderFromLiveOrderBoard() throws Exception {
        // Given
        OrderService orderService = new OrderServiceImpl();
        OrderSummaryService orderSummaryService = new OrderSummaryServiceImpl();

        placeOrder("user1", 3.5, 306, OrderType.SELL, orderService);
        Long order2 = placeOrder("user2", 1.2, 310, OrderType.SELL, orderService);
        placeOrder("user3", 1.5, 307, OrderType.SELL, orderService);
        placeOrder("user4", 2.0, 306, OrderType.SELL, orderService);

        List<OrderSummary> summaryOrders = orderSummaryService.getSummaryOrders(orderService.getLiveOrders());

        // When
        List<String> placedOrders = liveOrderBoard.renderSummaryOrderInformation(summaryOrders);

        //Then
        assertThat(placedOrders.get(0), is("5.50 kg for £306"));
        assertThat(placedOrders.get(1), is("1.50 kg for £307"));
        assertThat(placedOrders.get(2), is("1.20 kg for £310"));

        // Cancel order 2
        cancelOrder(order2, orderService);
        List<OrderSummary> summaryOrdersPostCancellation = orderSummaryService.getSummaryOrders(orderService.getLiveOrders());
        List<String> ordersPostCancellation = liveOrderBoard.renderSummaryOrderInformation(summaryOrdersPostCancellation);

        //Then
        assertThat(ordersPostCancellation.get(0), is("5.50 kg for £306"));
        assertThat(ordersPostCancellation.get(1), is("1.50 kg for £307"));
        assertThat(ordersPostCancellation.size(), is(2));

    }

    @Test
    public void shouldRenderBothSellAndBuyRegisteredOrders() throws Exception {
        // Given
        OrderService orderService = new OrderServiceImpl();
        OrderSummaryService orderSummaryService = new OrderSummaryServiceImpl();

        placeOrder("user1", 3.5, 306, OrderType.SELL, orderService);
        placeOrder("user2", 1.2, 315, OrderType.BUY, orderService);
        placeOrder("user3", 1.5, 312, OrderType.BUY, orderService);
        placeOrder("user4", 2.0, 298, OrderType.SELL, orderService);
        placeOrder("user5", 3.5, 302, OrderType.SELL, orderService);
        placeOrder("user6", 1.0, 312, OrderType.BUY, orderService);
        placeOrder("user7", 2.6, 311, OrderType.BUY, orderService);
        placeOrder("user8", 1.4, 306, OrderType.SELL, orderService);

        List<OrderSummary> summaryOrders = orderSummaryService.getSummaryOrders(orderService.getLiveOrders());

        // When
        List<String> renderedOrders = liveOrderBoard.renderSummaryOrderInformation(summaryOrders);
        assertThat(renderedOrders.get(0), is("2.00 kg for £298"));
        assertThat(renderedOrders.get(1), is("3.50 kg for £302"));
        assertThat(renderedOrders.get(2), is("4.90 kg for £306"));
        assertThat(renderedOrders.get(3), is("1.20 kg for £315"));
        assertThat(renderedOrders.get(4), is("2.50 kg for £312"));
        assertThat(renderedOrders.get(5), is("2.60 kg for £311"));
    }

    @Test
    public void shouldRenderBothSellAndBuyRegisteredOrdersMinusCancelledOrders() throws Exception {
        // Given
        OrderService orderService = new OrderServiceImpl();
        OrderSummaryService orderSummaryService = new OrderSummaryServiceImpl();

        placeOrder("user1", 3.5, 306, OrderType.SELL, orderService);
        placeOrder("user2", 1.2, 315, OrderType.BUY, orderService);
        placeOrder("user3", 1.5, 312, OrderType.BUY, orderService);
        placeOrder("user4", 2.0, 298, OrderType.SELL, orderService);
        placeOrder("user5", 3.5, 302, OrderType.SELL, orderService);
        Long orderToCancel = placeOrder("user6", 1.0, 312, OrderType.BUY, orderService);
        placeOrder("user7", 2.6, 311, OrderType.BUY, orderService);
        placeOrder("user8", 1.4, 306, OrderType.SELL, orderService);

        cancelOrder(orderToCancel, orderService);
        List<OrderSummary> summaryOrders = orderSummaryService.getSummaryOrders(orderService.getLiveOrders());

        // When
        List<String> renderedOrders = liveOrderBoard.renderSummaryOrderInformation(summaryOrders);
        assertThat(renderedOrders.get(0), is("2.00 kg for £298"));
        assertThat(renderedOrders.get(1), is("3.50 kg for £302"));
        assertThat(renderedOrders.get(2), is("4.90 kg for £306"));
        assertThat(renderedOrders.get(3), is("1.20 kg for £315"));
        assertThat(renderedOrders.get(4), is("1.50 kg for £312"));
        assertThat(renderedOrders.get(5), is("2.60 kg for £311"));
    }

    private Long placeOrder(String userId, double qty, double price, OrderType orderType, OrderService orderService) {
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(qty), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(price), Currency.getInstance("GBP")), OrderUnit.KILO);
        return orderService.registerOrder(userId, orderQuantity, unitPrice, orderType);
    }

    private void cancelOrder(Long orderId, OrderService orderService) throws OrderNotFoundException {
        orderService.cancelOrder(orderId);
    }
}