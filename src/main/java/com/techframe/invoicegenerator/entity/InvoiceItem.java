package com.techframe.invoicegenerator.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvoiceItem {
    private Product product;
    private Integer quantity;
    private BigDecimal total;
    private BigDecimal vat;

    public InvoiceItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        total = product.getDiscountPrice().multiply(new BigDecimal(quantity));
        vat = total.multiply(product.getVat().divide(new BigDecimal(100), RoundingMode.DOWN));
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
