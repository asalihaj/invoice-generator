package com.techframe.invoicegenerator.service;

import com.techframe.invoicegenerator.entity.Invoice;
import com.techframe.invoicegenerator.entity.InvoiceItem;
import com.techframe.invoicegenerator.entity.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class OrderService {

    //TODO: Refactor this method
    public List<Invoice> createOrder(List<InvoiceItem> items) {
        List<Invoice> invoiceList = new ArrayList<>();
        items.sort((item1, item2) -> {
            BigDecimal price1 = item1.getProduct().getTotalPrice();
            BigDecimal price2 = item2.getProduct().getTotalPrice();
            return price2.compareTo(price1);
        });

        int totalQuantity = cumulativeQuantity(items);

        while (totalQuantity > 0) {
            Invoice invoice = new Invoice();

            for (int i = 0; i < items.size(); i++) {
                int quantity = items.get(i).getQuantity();
                if (quantity == 0) {
                    continue;
                }

                Product product = items.get(i).getProduct();
                boolean isSPI = items.get(i).getProduct().getTotalPrice().compareTo(new BigDecimal(500)) > 0;
                if (isSPI) {
                    Invoice inv = new Invoice();
                    inv.addSingleProduct(product);
                    invoiceList.add(inv);
                    continue;
                }

                int maxQuantity = calculateMaxQuantity(
                        product.getTotalPrice(),
                        quantity,
                        new BigDecimal(500).subtract(invoice.getTotalAmount())
                );
                boolean isAdded = invoice.addProduct(items.get(i).getProduct(), maxQuantity);
                if (isAdded) {
                    totalQuantity -= maxQuantity;
                    items.get(i).setQuantity(quantity - maxQuantity);
                }
            }
            invoiceList.add(invoice);
        }

        return invoiceList;
    }



    private int cumulativeQuantity(List<InvoiceItem> items) {
        return items.stream().map(InvoiceItem::getQuantity).reduce(0, Integer::sum);
    }

    private int calculateMaxQuantity(BigDecimal productPrice, int quantity, BigDecimal limit) {
        int maxQuantityByProducts = Math.min(50, limit.divide(productPrice, RoundingMode.HALF_DOWN).intValue());
        return Math.min(maxQuantityByProducts, quantity);
    }

}
