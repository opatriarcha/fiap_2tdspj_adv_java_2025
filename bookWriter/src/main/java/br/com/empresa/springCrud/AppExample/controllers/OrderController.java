package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.Order;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderKey;
import br.com.empresa.springCrud.AppExample.dtos.OrderDTO;
import br.com.empresa.springCrud.AppExample.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // --- CRUD básico ---

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll() {
        List<OrderDTO> orders = orderService.findAll()
                .stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<OrderDTO>> findAllPaged(Pageable pageable) {
        Page<OrderDTO> page = orderService.findAllPaged(pageable)
                .map(OrderDTO::fromEntity);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderDTO> findById(@PathVariable UUID userId, @PathVariable Long orderId) {
        OrderKey key = new OrderKey(userId, orderId);
        return orderService.findById(key)
                .map(order -> ResponseEntity.ok(OrderDTO.fromEntity(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderDTO orderDTO) {
        Order order = OrderDTO.toEntity(orderDTO);
        Order savedOrder = orderService.save(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userId}/{orderId}")
                .buildAndExpand(savedOrder.getId().getUserId(), savedOrder.getId().getOrderId())
                .toUri();
        return ResponseEntity.created(location).body(OrderDTO.fromEntity(savedOrder));
    }

    @PutMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderDTO> update(@PathVariable UUID userId, @PathVariable Long orderId, @Valid @RequestBody OrderDTO orderDTO) {
        OrderKey key = new OrderKey(userId, orderId);
        if (!orderService.existsById(key)) {
            return ResponseEntity.notFound().build();
        }
        Order order = OrderDTO.toEntity(orderDTO);
        order.setId(key);
        Order updatedOrder = orderService.save(order);
        return ResponseEntity.ok(OrderDTO.fromEntity(updatedOrder));
    }

    @PatchMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderDTO> partialUpdate(@PathVariable UUID userId, @PathVariable Long orderId, @RequestBody Map<String, Object> updates) {
        OrderKey key = new OrderKey(userId, orderId);
        if (!orderService.existsById(key)) {
            return ResponseEntity.notFound().build();
        }
        Order updatedOrder = orderService.partialUpdate(key, updates);
        return ResponseEntity.ok(OrderDTO.fromEntity(updatedOrder));
    }

    @DeleteMapping("/{userId}/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId, @PathVariable Long orderId) {
        OrderKey key = new OrderKey(userId, orderId);
        if (!orderService.existsById(key)) {
            return ResponseEntity.notFound().build();
        }
        orderService.deleteById(key);
        return ResponseEntity.noContent().build();
    }

    // --- Consultas especiais ---

    @GetMapping("/search/productName")
    public ResponseEntity<List<OrderDTO>> searchByProductName(@RequestParam String productName) {
        List<OrderDTO> orders = orderService.searchByProductName(productName)
                .stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<OrderDTO>> searchByPrice(@RequestParam double price) {
        List<OrderDTO> orders = orderService.searchByPriceGreaterThan(price)
                .stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/search/quantity/querydsl")
    public ResponseEntity<List<OrderDTO>> searchByQuantityQueryDSL(@RequestParam int minQuantity) {
        List<OrderDTO> orders = orderService.searchByQuantityGreaterThanQueryDSL(minQuantity)
                .stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/search/quantity/criteria")
    public ResponseEntity<List<OrderDTO>> searchByQuantityCriteria(@RequestParam int minQuantity) {
        List<OrderDTO> orders = orderService.searchByQuantityGreaterThanCriteria(minQuantity)
                .stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }
}
