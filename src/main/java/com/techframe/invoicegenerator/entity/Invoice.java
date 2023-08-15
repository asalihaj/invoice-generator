package com.techframe.invoicegenerator.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private String id;
    private List<InvoiceItem> products;
    private BigDecimal totalAmount;

    public Invoice() {
        this(new ArrayList<>(), new BigDecimal(0));
    }

    public Invoice(List<InvoiceItem> products, BigDecimal totalAmount) {
        this.products = products;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<InvoiceItem> getProducts() {
        return products;
    }

    public void setProducts(List<InvoiceItem> products) {
        this.products = products;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
