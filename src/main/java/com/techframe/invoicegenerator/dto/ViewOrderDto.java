package com.techframe.invoicegenerator.dto;

import java.math.BigDecimal;

public class ViewOrderDto {
    private String id;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private BigDecimal vat;
    private int numberOfInvoices;

    public ViewOrderDto(String id, BigDecimal subTotal, BigDecimal totalAmount, BigDecimal vat, int numberOfInvoices) {
        this.id = id;
        this.subTotal = subTotal;
        this.totalAmount = totalAmount;
        this.vat = vat;
        this.numberOfInvoices = numberOfInvoices;
    }
}
