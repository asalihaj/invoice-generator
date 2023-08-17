package com.techframe.invoicegenerator.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private List<Invoice> invoices;
    private BigDecimal subTotal;
    private BigDecimal vat;
    private BigDecimal total;

    private static final String PREFIX = "ORD";
    private static final int ID_LENGTH = 5;
    private static int counter = 1;

    public Order() {
        this(new ArrayList<>());
    }

    public Order(List<Invoice> invoices) {
        this.id = generateId();
        this.invoices = invoices;
        total = calculateTotal();
        subTotal = calculateSubTotal();
        vat = total.subtract(subTotal);
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    private BigDecimal calculateTotal() {
        BigDecimal total = new BigDecimal(0);
        for (Invoice invoice : invoices) {
            total = total.add(invoice.calculateTotalAmount());
        }

        return total;
    }

    private BigDecimal calculateSubTotal() {
        BigDecimal subTotal = new BigDecimal(0);
        for (Invoice invoice : invoices) {
            subTotal = subTotal.add(invoice.calculateSubTotal());
        }

        return subTotal;
    }

    private String generateId() {
        String idNumber = String.format("%0" + (ID_LENGTH - PREFIX.length()) + "d", counter++);
        return PREFIX + idNumber;
    }
}
