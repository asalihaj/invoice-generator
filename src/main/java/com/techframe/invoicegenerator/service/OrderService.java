package com.techframe.invoicegenerator.service;

import com.techframe.invoicegenerator.dto.ViewOrderDto;
import com.techframe.invoicegenerator.entity.Invoice;
import com.techframe.invoicegenerator.entity.InvoiceItem;
import com.techframe.invoicegenerator.entity.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private List<Order> orders = new ArrayList<>();
    private final InvoiceService invoiceService;

    public OrderService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public Order createOrder(List<InvoiceItem> items) {
        List<Invoice> invoiceList = new ArrayList<>();
        sortByRatio(items);

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

        Order order = new Order(invoiceList);
        orders.add(order);

        return order;
    }

    private void sortByRatio(List<InvoiceItem> items) {
        items.sort(Comparator.comparing(item ->
                item.getProduct().getTotalPrice()
                        .divide(new BigDecimal(item.getQuantity()), 6, RoundingMode.HALF_DOWN))
        );
        Collections.reverse(items);
    }

    private int cumulativeQuantity(List<InvoiceItem> items) {
        return items.stream().map(InvoiceItem::getQuantity).reduce(0, Integer::sum);
    }

    public List<ViewOrderDto> list() {
        List<ViewOrderDto> viewOrderDtos = orders.stream().map(o -> new ViewOrderDto(
                o.getId(),
                o.getSubTotal(),
                o.getTotal(),
                o.getVat(),
                o.getInvoices().size()
        )).toList();

        return viewOrderDtos;
    }

    public Order getOrderDetails(String id) {
        return orders.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);
    }
}
