package com.techframe.invoicegenerator.service;

import com.techframe.invoicegenerator.entity.Invoice;
import com.techframe.invoicegenerator.entity.InvoiceItem;
import com.techframe.invoicegenerator.entity.Order;
import com.techframe.invoicegenerator.entity.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class OrderService {

    //TODO: Refactor this method
    public Order createOrder(List<InvoiceItem> items) {
        List<Invoice> invoiceList = new ArrayList<>();
        items.sort((item1, item2) -> {
            BigDecimal price1 = item1.getProduct().getTotalPrice();
            BigDecimal price2 = item2.getProduct().getTotalPrice();
            return price1.compareTo(price2);
        });

        int totalQuantity = cumulativeQuantity(items);
        int unchangedIterations = 0;

        while (totalQuantity > 0) {
            Invoice invoice = new Invoice();
            boolean quantityChanged = false;

            for (InvoiceItem item : items) {
                int quantity = item.getQuantity();
                if (quantity == 0) {
                    continue;
                }

                boolean isSPI = item.getProduct().getTotalPrice().compareTo(new BigDecimal(500)) > 0;
                if (isSPI) {
                    Invoice inv = new Invoice();
                    boolean isAdded = inv.addSingleProduct(item);
                    if (isAdded) {
                        invoiceList.add(inv);
                        item.setQuantity(quantity - 1);
                        totalQuantity--;
                        quantityChanged = true;
                        continue;
                    }
                }

                int maxQuantity = calculateMaxQuantity(
                        item.getProduct().getTotalPrice(),
                        quantity,
                        new BigDecimal(500).subtract(invoice.getTotalAmount())
                );
                if (maxQuantity == 0) break;
                boolean isAdded = invoice.addProduct(new InvoiceItem(item.getProduct(), maxQuantity));
                if (isAdded) {
                    totalQuantity -= maxQuantity;
                    item.setQuantity(quantity - maxQuantity);
                    quantityChanged = true;
                }
            }

            if (quantityChanged) {
                unchangedIterations = 0;
            } else {
                unchangedIterations++;
                if (unchangedIterations > 5) {
                    throw new RuntimeException("Error while creating order");
                }
            }
            invoiceList.add(invoice);
        }

        return new Order(invoiceList);
    }

    private int cumulativeQuantity(List<InvoiceItem> items) {
        return items.stream().map(InvoiceItem::getQuantity).reduce(0, Integer::sum);
    }

    private int calculateMaxQuantity(BigDecimal productPrice, int quantity, BigDecimal limit) {
        int maxQuantityByProducts = Math.min(50, limit.divide(productPrice, RoundingMode.HALF_DOWN).intValue());
        return Math.min(maxQuantityByProducts, quantity);
    }

}
