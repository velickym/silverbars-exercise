package com.velicky.martin.silverbars;

class Order {

    private final String userId;
    private final OrderType type;
    private final int grams;
    private final int pricePerKg;

    Order(String userId, OrderType type, int grams, int pricePerKg) {
        this.userId = userId;
        this.type = type;
        this.grams = grams;
        this.pricePerKg = pricePerKg;
    }

    String getUserId() {
        return userId;
    }

    int getGrams() {
        return grams;
    }

    int getPricePerKg() {
        return pricePerKg;
    }

    OrderType getType() {
        return type;
    }
}
