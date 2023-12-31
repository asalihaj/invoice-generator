package com.techframe.invoicegenerator.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvoiceItem {
    private Product product;
    private Integer quantity;
    private BigDecimal subTotal; // does not include VAT
    private BigDecimal vat;

    public InvoiceItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        subTotal = product.getDiscountPrice().multiply(new BigDecimal(quantity));
        vat = subTotal.multiply(product.getVat().divide(new BigDecimal(100), 4, RoundingMode.HALF_DOWN))
                .setScale(4, RoundingMode.HALF_DOWN);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
