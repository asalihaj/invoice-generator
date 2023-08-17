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

    public Order createOrder(List<InvoiceItem> items) {
        List<Invoice> invoiceList = new ArrayList<>();
        items.sort(Comparator.comparing(item -> item.getProduct().getTotalPrice()));

        int totalQuantity = cumulativeQuantity(items);
        int unchangedIterations = 0;
        int maxTries = 5;

        while (totalQuantity > 0) {
            Invoice invoice = createInvoice(items);
            int addedQuantity = invoice.getProducts().stream().mapToInt(InvoiceItem::getQuantity).sum();
            totalQuantity = cumulativeQuantity(items);

            if (addedQuantity > 0) {
                unchangedIterations = 0;
            } else {
                unchangedIterations++;
                if (unchangedIterations >= maxTries) {
                    throw new RuntimeException("Error while creating order");
                }
            }

            invoiceList.add(invoice);
        }

        return new Order(invoiceList);
    }

    private Invoice createInvoice(List<InvoiceItem> items) {
        Invoice invoice = new Invoice();

        for (InvoiceItem item : items) {
            int quantity = item.getQuantity();

            if (quantity == 0) {
                continue;
            }

            boolean isAdded = addItemsToInvoice(item, invoice);
            if (!isAdded) {
                break;
            }
        }

        return invoice;
    }

    private boolean addItemsToInvoice(InvoiceItem item, Invoice invoice) {
        boolean isSPI = item.getProduct().getTotalPrice().compareTo(new BigDecimal(500)) > 0;
        if (isSPI) {
            Invoice inv = new Invoice();
            boolean isAdded = inv.addSingleProduct(item);
            item.setQuantity(item.getQuantity() - 1);

            return isAdded;
        }

        int maxQuantity = calculateMaxQuantity(
                item.getProduct().getTotalPrice(),
                item.getQuantity(),
                new BigDecimal(500).subtract(invoice.getTotalAmount())
        );
        if (maxQuantity == 0) {
            return false;
        }

        boolean isAdded = invoice.addProduct(new InvoiceItem(item.getProduct(), maxQuantity));
        item.setQuantity(item.getQuantity() - maxQuantity);

        return isAdded;
    }

    private int cumulativeQuantity(List<InvoiceItem> items) {
        return items.stream().map(InvoiceItem::getQuantity).reduce(0, Integer::sum);
    }

    private int calculateMaxQuantity(BigDecimal productPrice, int quantity, BigDecimal limit) {
        int maxQuantityByProducts = Math.min(50, limit.divide(productPrice, RoundingMode.HALF_DOWN).intValue());
        return Math.min(maxQuantityByProducts, quantity);
    }

}
