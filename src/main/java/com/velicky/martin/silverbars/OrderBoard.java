package com.velicky.martin.silverbars;

import java.util.*;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.groupingBy;

@SuppressWarnings("WeakerAccess") // meant to be used as a library
public class OrderBoard {

    private static boolean isOrderInvalid(String userId, OrderType type, int grams, int pricePerKg) {
        return userId == null || userId.length() == 0 || type == null || grams <= 0 || pricePerKg <=0;
    }

    private final List<Order> orders;

    public OrderBoard() {
        orders = new LinkedList<>();
    }

    OrderBoard(List<Order> orders) {
        this.orders = orders;
    }

    public Order register(final String userId, final OrderType type, final int grams, final int pricePerKg) {

        if (isOrderInvalid(userId, type, grams, pricePerKg))
            throw new IllegalArgumentException("Invalid order");

        final Order order = new Order(userId, type, grams, pricePerKg);
        orders.add(order);
        return order;
    }

    public boolean cancel(final Order order) {
        return orders.remove(order);
    }

    public SortedMap<Integer, Integer> getOrders(OrderType type) {

        final Comparator<Integer> comparator = type == OrderType.SELL ? naturalOrder() : reverseOrder();
        final TreeMap<Integer, Integer> map = orders.stream()
            .filter(order -> order.getType() == type)
            .collect(groupingBy(Order::getPricePerKg, () -> new TreeMap<>(comparator), summingInt(Order::getGrams)));

        return Collections.unmodifiableNavigableMap(map);
    }

}