package com.techframe.invoicegenerator.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
    private String name;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal vat;

    public Product(String name, BigDecimal price, BigDecimal vat) {
        this(name, price, new BigDecimal(0), vat);
    }

    public Product(String name, BigDecimal price, BigDecimal discount, BigDecimal vat) {
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.vat = vat;
    }

    public BigDecimal getPriceAfterDiscountAndVAT() {
        BigDecimal discountedPrice = price.subtract(discount);
        BigDecimal vatAmount = discountedPrice.multiply(vat.divide(new BigDecimal(100), RoundingMode.DOWN));
        return discountedPrice.add(vatAmount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }
}
