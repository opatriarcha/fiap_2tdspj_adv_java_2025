package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderItemKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderItemService {

    List<OrderItem> findAll();

    Page<OrderItem> findAllPaged(Pageable pageable);

    Optional<OrderItem> findById(OrderItemKey id);

    OrderItem save(OrderItem orderItem);

    void deleteById(OrderItemKey id);

    OrderItem partialUpdate(OrderItemKey id, Map<String, Object> updates);

    boolean existsById(OrderItemKey id);

    List<OrderItem> searchByItemName(String itemName);

    List<OrderItem> searchByQuantityGreaterThanQueryDSL(int quantityMin);

    List<OrderItem> searchByQuantityGreaterThanCriteria(int quantityMin);
}
