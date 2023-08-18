package com.techframe.invoicegenerator.entity;

import com.techframe.invoicegenerator.util.IdGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private String id;
    private List<InvoiceItem> products;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private BigDecimal vat;

    public static final BigDecimal MAX_AMOUNT = new BigDecimal(500);
    public static final Integer PRODUCT_LIMIT = 50;
    private static final String PREFIX = "INV";
    private static final int ID_LENGTH = 3;
    private static int counter = 1;

    public Invoice() {
        this(new ArrayList<>(), new BigDecimal(0), new BigDecimal(0));
    }

    public Invoice(List<InvoiceItem> products, BigDecimal subTotal, BigDecimal totalAmount) {
        this.id = IdGenerator.generateId(PREFIX, ID_LENGTH, counter++);
        this.products = products;
        this.subTotal = subTotal;
        this.totalAmount = totalAmount;
    }

    public boolean addProduct(InvoiceItem item) {
        if (canAddProduct(item)) {
            int index = products.indexOf(item);
            if (index == -1) {
                products.add(item);
            } else {
                InvoiceItem existingItem = products.get(index);
                Integer totalQuantity = item.getQuantity() + existingItem.getQuantity();
                item.setQuantity(totalQuantity);
                products.set(index, item);
            }

            updateInvoiceTotals();
            return true;
        }

        return false;
    }

    public boolean addSingleProduct(InvoiceItem item) {
        if (products.size() > 0) {
            return false;
        }
        item.setQuantity(1);
        products.add(item);

        updateInvoiceTotals();
        return true;
    }

    private void updateInvoiceTotals() {
        totalAmount = calculateTotalAmount();
        subTotal = calculateSubTotal();
        vat = totalAmount.subtract(subTotal);
    }

    public boolean canAddProduct(InvoiceItem item) {
        if (totalAmount.compareTo(MAX_AMOUNT) > 0) return false;

        int index = products.indexOf(item);
        if (index != -1) {
            InvoiceItem currentItem = products.get(index);
            Integer currentQuantity = currentItem.getQuantity();
            if (currentQuantity == null) currentQuantity = 0;

            int totalQuantity = currentQuantity + item.getQuantity();
            if (totalQuantity > PRODUCT_LIMIT) return false;
        }
        BigDecimal price = item.getProduct().getTotalPrice().multiply(new BigDecimal(item.getQuantity()));

        BigDecimal totalPrice = totalAmount.add(price);
        return totalPrice.compareTo(MAX_AMOUNT) <= 0;
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

    public BigDecimal calculateTotalAmount() {
        BigDecimal total = new BigDecimal(0);
        for (InvoiceItem item : products) {
            BigDecimal productPrice = item.getProduct().getTotalPrice();
            int quantity = item.getQuantity();

            total = total.add(productPrice.multiply(new BigDecimal(quantity)));
        }
        return total;
    }

    public BigDecimal calculateSubTotal() {
        BigDecimal subTotal = new BigDecimal(0);
        for (InvoiceItem item : products) {
            BigDecimal productPrice = item.getProduct().getDiscountPrice();
            int quantity = item.getQuantity();

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
}
