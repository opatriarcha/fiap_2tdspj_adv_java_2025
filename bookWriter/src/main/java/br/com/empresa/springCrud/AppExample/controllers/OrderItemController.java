package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderItemKey;
import br.com.empresa.springCrud.AppExample.dtos.OrderItemDTO;
import br.com.empresa.springCrud.AppExample.services.OrderItemService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> findAll() {
        List<OrderItemDTO> items = orderItemService.findAll()
                .stream()
                .map(OrderItemDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<OrderItemDTO>> findAllPaged(Pageable pageable) {
        Page<OrderItemDTO> page = orderItemService.findAllPaged(pageable)
                .map(OrderItemDTO::fromEntity);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{orderId}/{itemId}")
    public ResponseEntity<OrderItemDTO> findById(@PathVariable Long orderId, @PathVariable Long itemId) {
        OrderItemKey key = new OrderItemKey(orderId, itemId);
        return orderItemService.findById(key)
                .map(item -> ResponseEntity.ok(OrderItemDTO.fromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> create(@Valid @RequestBody OrderItemDTO dto) {
        OrderItem saved = orderItemService.save(OrderItemDTO.toEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}/{itemId}")
                .buildAndExpand(saved.getId().getOrderId(), saved.getId().getItemId())
                .toUri();
        return ResponseEntity.created(location).body(OrderItemDTO.fromEntity(saved));
    }

    @PutMapping("/{orderId}/{itemId}")
    public ResponseEntity<OrderItemDTO> update(@PathVariable Long orderId, @PathVariable Long itemId, @Valid @RequestBody OrderItemDTO dto) {
        OrderItemKey key = new OrderItemKey(orderId, itemId);
        if (!orderItemService.existsById(key)) {
            return ResponseEntity.notFound().build();
        }
        OrderItem orderItem = OrderItemDTO.toEntity(dto);
        orderItem.setId(key);
        OrderItem updated = orderItemService.save(orderItem);
        return ResponseEntity.ok(OrderItemDTO.fromEntity(updated));
    }

    @PatchMapping("/{orderId}/{itemId}")
    public ResponseEntity<OrderItemDTO> partialUpdate(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody Map<String, Object> updates) {
        OrderItemKey key = new OrderItemKey(orderId, itemId);
        if (!orderItemService.existsById(key)) {
            return ResponseEntity.notFound().build();
        }
        OrderItem updated = orderItemService.partialUpdate(key, updates);
        return ResponseEntity.ok(OrderItemDTO.fromEntity(updated));
    }

    @DeleteMapping("/{orderId}/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId, @PathVariable Long itemId) {
        OrderItemKey key = new OrderItemKey(orderId, itemId);
        if (!orderItemService.existsById(key)) {
            return ResponseEntity.notFound().build();
        }
        orderItemService.deleteById(key);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/itemName")
    public ResponseEntity<List<OrderItemDTO>> searchByItemName(@RequestParam String itemName) {
        List<OrderItemDTO> items = orderItemService.searchByItemName(itemName)
                .stream()
                .map(OrderItemDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/quantity/querydsl")
    public ResponseEntity<List<OrderItemDTO>> searchByQuantityQueryDSL(@RequestParam int minQuantity) {
        List<OrderItemDTO> items = orderItemService.searchByQuantityGreaterThanQueryDSL(minQuantity)
                .stream()
                .map(OrderItemDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/quantity/criteria")
    public ResponseEntity<List<OrderItemDTO>> searchByQuantityCriteria(@RequestParam int minQuantity) {
        List<OrderItemDTO> items = orderItemService.searchByQuantityGreaterThanCriteria(minQuantity)
                .stream()
                .map(OrderItemDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }
}
