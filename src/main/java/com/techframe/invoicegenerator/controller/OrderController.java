package com.techframe.invoicegenerator.controller;

import com.techframe.invoicegenerator.dto.ViewOrderDto;
import com.techframe.invoicegenerator.entity.InvoiceItem;
import com.techframe.invoicegenerator.entity.Order;
import com.techframe.invoicegenerator.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    public ResponseEntity<List<ViewOrderDto>> getAllOrders() {
        List<ViewOrderDto> orders = orderService.list();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<Order> getOrderDetails(@PathVariable String id) {
        Order order = orderService.getOrderDetails(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody List<InvoiceItem> items) {
        Order order = orderService.createOrder(items);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
