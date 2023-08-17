package com.techframe.invoicegenerator.service;

import com.techframe.invoicegenerator.entity.Invoice;
import com.techframe.invoicegenerator.entity.InvoiceItem;
import com.techframe.invoicegenerator.entity.Order;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final InvoiceService invoiceService;

    public OrderService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public Order createOrder(List<InvoiceItem> items) {
        List<Invoice> invoiceList = new ArrayList<>();
        items.sort(Comparator.comparing(item -> item.getProduct().getTotalPrice()));

        int totalQuantity = cumulativeQuantity(items);
        int unchangedIterations = 0;
        int maxTries = 5;

        while (totalQuantity > 0) {
            Invoice invoice = invoiceService.createInvoice(items);
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

    private int cumulativeQuantity(List<InvoiceItem> items) {
        return items.stream().map(InvoiceItem::getQuantity).reduce(0, Integer::sum);
    }
}
