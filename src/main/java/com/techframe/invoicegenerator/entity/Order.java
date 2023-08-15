package com.techframe.invoicegenerator.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private List<Invoice> invoices;
    private BigDecimal subTotal;
    private BigDecimal vat;

    public Order() {
        this(new ArrayList<>());
    }

    public Order(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
