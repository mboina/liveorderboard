package com.sbm.orders.liveorderboard.display;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.sbm.orders.liveorderboard.domain.OrderSummary;

public class LiveOrderBoard {
    private static final String SUMMARY_INFORMATION_FORMAT = "{0} {1} for {2}{3}";

    public List<String> renderSummaryOrderInformation(List<OrderSummary> summaryOrders) {
        return summaryOrders.stream()
                        .map(orderSummary -> MessageFormat.format(SUMMARY_INFORMATION_FORMAT,
                                        orderSummary.getOrderQuantity().getQuantity().toPlainString(),
                                        orderSummary.getOrderQuantity().getOrderUnit().getValue(),
                                        orderSummary.getUnitPrice().getPrice().getCurrency().getSymbol(),
                                        orderSummary.getUnitPrice().getPrice().getAmount()))
                        .collect(Collectors.toList());
    }
}
