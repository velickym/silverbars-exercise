package com.velicky.martin.silverbars;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

class OrderBoardTest {

    @Test
    void registersAnOrder() {

        // given
        final String userId = "Martin";
        final int quantity = 2222;
        final int price = 222;
        final OrderType type = OrderType.SELL;

        // when
        final ArrayList<Order> orders = new ArrayList<>();
        final OrderBoard orderBoard = new OrderBoard(orders);
        final Order order = orderBoard.register(userId, type, quantity, price);

        // then
        Assertions.assertNotNull(order);
        Assertions.assertEquals(userId, order.getUserId());
        Assertions.assertEquals(type, order.getType());
        Assertions.assertEquals(quantity, order.getGrams());
        Assertions.assertEquals(price, order.getPricePerKg());
        Assertions.assertEquals(1, orders.size());
        Assertions.assertSame(orders.get(0), order);
    }

    @Test
    void doesNotRegisterInvalidOrder() {
        final OrderBoard board = new OrderBoard();
        final Class<IllegalArgumentException> e = IllegalArgumentException.class;
        Assertions.assertThrows(e, () -> board.register(null, null, 0, 0));
        Assertions.assertThrows(e, () -> board.register("", null, 0, 0));
        Assertions.assertThrows(e, () -> board.register("Martin", null, 0, 0));
        Assertions.assertThrows(e, () -> board.register("Martin", OrderType.SELL, -5, 0));
        Assertions.assertThrows(e, () -> board.register("Martin", OrderType.SELL, -5, -6));
    }

    @Test
    void cancelsAnOrder() {

        // given
        final Order order = new Order("Martin", OrderType.SELL, 2222, 222);
        final List<Order> list = new LinkedList<>();
        list.add(order);
        final OrderBoard board = new OrderBoard(list);

        // when
        final boolean orderCancelled = board.cancel(order);

        // then
        Assertions.assertTrue(orderCancelled);
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    void getsEmptyOrders() {
        final SortedMap<Integer, Integer> sellOrders = new OrderBoard().getOrders(OrderType.SELL);
        Assertions.assertNotNull(sellOrders);
        Assertions.assertTrue(sellOrders.isEmpty());
        final SortedMap<Integer, Integer> buyOrders = new OrderBoard().getOrders(OrderType.BUY);
        Assertions.assertNotNull(buyOrders);
        Assertions.assertTrue(buyOrders.isEmpty());
    }

    @Test
    void getsGroupedOrders() {

        // given
        final List<Order> list = new LinkedList<>();
        list.add(new Order("Jon", OrderType.SELL, 3500, 306));
        list.add(new Order("Andy", OrderType.SELL, 1200, 310));
        list.add(new Order("Chris", OrderType.SELL, 1500, 307));
        list.add(new Order("George", OrderType.SELL, 2000, 306));

        // when
        final OrderBoard board = new OrderBoard(list);
        final SortedMap<Integer, Integer> orders = board.getOrders(OrderType.SELL);

        // then
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(3, orders.size());
        Assertions.assertEquals(5500, orders.get(306));
        Assertions.assertEquals(1200, orders.get(310));
        Assertions.assertEquals(1500, orders.get(307));
    }

    @Test
    void getsSellOrdersInAscendingOrder() {

        // given
        final List<Order> list = new LinkedList<>();
        list.add(new Order("Jon", OrderType.SELL, 1, 1));
        list.add(new Order("Andy", OrderType.SELL, 2, 2));
        list.add(new Order("Chris", OrderType.SELL, 3, 3));
        list.add(new Order("George", OrderType.SELL, 4, 4));

        // when
        final OrderBoard board = new OrderBoard(list);
        final SortedMap<Integer, Integer> orders = board.getOrders(OrderType.SELL);

        // then
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(4, orders.size());

        Integer last = 0;
        for (Integer price : orders.keySet()) {
            Assertions.assertTrue(price > last);
            last = price;
        }
    }

    @Test
    void getsBuyOrdersInAscendingOrder() {

        // given
        final List<Order> list = new LinkedList<>();
        list.add(new Order("Jon", OrderType.BUY, 1, 1));
        list.add(new Order("Andy", OrderType.BUY, 2, 2));
        list.add(new Order("Chris", OrderType.BUY, 3, 3));
        list.add(new Order("George", OrderType.BUY, 4, 4));

        // when
        final OrderBoard board = new OrderBoard(list);
        final SortedMap<Integer, Integer> orders = board.getOrders(OrderType.BUY);

        // then
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(4, orders.size());

        Integer last = Integer.MAX_VALUE;
        for (Integer price : orders.keySet()) {
            Assertions.assertTrue(price < last);
            last = price;
        }
    }

}