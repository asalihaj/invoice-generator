package com.techframe.invoicegenerator.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private List<Invoice> invoices;
    private BigDecimal subTotal;
    private BigDecimal vat;

    private static final String PREFIX = "ORD";
    private static final int ID_LENGTH = 5;
    private static int counter = 1;

    public Order() {
        this(new ArrayList<>());
    }

    public Order(List<Invoice> invoices) {
        this.id = generateId();
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

    private String generateId() {
        String idNumber = String.format("%0" + (ID_LENGTH - PREFIX.length()) + "d", counter++);
        return PREFIX + idNumber;
    }
}
