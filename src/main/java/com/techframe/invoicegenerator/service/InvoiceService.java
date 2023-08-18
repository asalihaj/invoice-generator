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

            addItemsToInvoice(item, invoice);
        }

        return invoice;
    }

    private boolean addItemsToInvoice(InvoiceItem item, Invoice invoice) {
        boolean isSPI = item.getProduct().getTotalPrice().compareTo(Invoice.MAX_AMOUNT) > 0;
        if (isSPI && invoice.getProducts().isEmpty()) {
            boolean isAdded = invoice.addSingleProduct(new InvoiceItem(item.getProduct(), 1));
            item.setQuantity(item.getQuantity() - 1);

            return isAdded;
        }

        int maxQuantity = calculateMaxQuantity(
                item.getProduct().getTotalPrice(),
                item.getQuantity(),
                Invoice.MAX_AMOUNT.subtract(invoice.getTotalAmount())
        );
        if (maxQuantity == 0) {
            return false;
        }

        boolean isAdded = invoice.addProduct(new InvoiceItem(item.getProduct(), maxQuantity));
        if (isAdded) {
            item.setQuantity(item.getQuantity() - maxQuantity);
        }

        return isAdded;
    }

    private int calculateMaxQuantity(BigDecimal productPrice, int quantity, BigDecimal limit) {
        int maxQuantityByProducts = Math.min(
                Invoice.PRODUCT_LIMIT,
                limit.divide(productPrice, RoundingMode.FLOOR).intValue()
        );
        return Math.min(maxQuantityByProducts, quantity);
    }
}
