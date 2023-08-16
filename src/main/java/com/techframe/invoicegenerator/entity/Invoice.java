package com.techframe.invoicegenerator.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Invoice {
    private String id;
    private Map<Product, Integer> products;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private BigDecimal vat;

    private static final String PREFIX = "INV";
    private static final int ID_LENGTH = 5;
    private static int counter = 1;

    public Invoice() {
        this(new HashMap<>(), new BigDecimal(0), new BigDecimal(0));
    }

    public Invoice(Map<Product, Integer> products, BigDecimal subTotal, BigDecimal totalAmount) {
        this.id = generateId();
        this.products = products;
        this.subTotal = subTotal;
        this.totalAmount = totalAmount;
    }

    public boolean addProduct(Product product, int quantity) {
        if (canAddProduct(product, quantity)) {
            Integer currentQuantity = products.get(product);
            if (currentQuantity == null) {
                products.put(product, quantity);
            } else {
                products.replace(product, quantity, quantity + currentQuantity);
            }
            totalAmount = calculateTotalAmount();
            subTotal = calculateSubTotal();
            vat = totalAmount.subtract(subTotal);

            return true;
        }

        return false;
    }

    public boolean addSingleProduct(Product product) {
        if (products.size() > 0) {
            return false;
        }

        products.put(product, 1);
        return true;
    }

    public boolean canAddProduct(Product product, int quantity) {
        if (totalAmount.compareTo(new BigDecimal(500)) > 0) return false;

        BigDecimal price = product.getTotalPrice().multiply(new BigDecimal(quantity));
        if (products.get(product) != null) {
            Integer currentQuantity = products.get(product);
            if (currentQuantity == null) currentQuantity = 0;

            int totalQuantity = currentQuantity + quantity;
            if (totalQuantity > 50) return false;
        }

        BigDecimal totalPrice = totalAmount.add(price);
        return totalPrice.compareTo(new BigDecimal(500)) <= 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    public BigDecimal calculateTotalAmount() {
        BigDecimal total = new BigDecimal(0);
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            BigDecimal productPrice = entry.getKey().getTotalPrice();
            int quantity = entry.getValue();

            total = total.add(productPrice.multiply(new BigDecimal(quantity)));
        }
        return total;
    }

    public BigDecimal calculateSubTotal() {
        BigDecimal subTotal = new BigDecimal(0);
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            BigDecimal productPrice = entry.getKey().getDiscountPrice();
            int quantity = entry.getValue();

            subTotal = subTotal.add(productPrice.multiply(new BigDecimal(quantity)));
        }
        return subTotal;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    private String generateId() {
        String idNumber = String.format("%0" + (ID_LENGTH - PREFIX.length()) + "d", counter++);
        return PREFIX + idNumber;
    }
}
