package com.sbm.orders.liveorderboard.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Represents the price object.
 */
public class Price {
    private final BigDecimal amount;
    private final Currency currency;

    public Price(BigDecimal amount, Currency currency) {
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Price price = (Price) obj;
        return Objects.equals(amount, price.amount) &&
                        Objects.equals(currency, price.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                        .add("amount", amount)
                        .add("currency", currency)
                        .toString();
    }
}
