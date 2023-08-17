package com.techframe.invoicegenerator.service;

import com.techframe.invoicegenerator.entity.Invoice;
import com.techframe.invoicegenerator.entity.InvoiceItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvoiceService {

    public Invoice createInvoice(List<InvoiceItem> items) {
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

    private int calculateMaxQuantity(BigDecimal productPrice, int quantity, BigDecimal limit) {
        int maxQuantityByProducts = Math.min(50, limit.divide(productPrice, RoundingMode.HALF_DOWN).intValue());
        return Math.min(maxQuantityByProducts, quantity);
    }
}
