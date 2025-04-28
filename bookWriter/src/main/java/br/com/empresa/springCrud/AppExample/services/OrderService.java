package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Order;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();

    Page<Order> findAllPaged(Pageable pageable);

    Optional<Order> findById(OrderKey id);

    Order save(Order order);

    void deleteById(OrderKey id);

    Order partialUpdate(OrderKey id, Map<String, Object> updates);

    boolean existsById(OrderKey id);

    List<Order> searchByProductName(String productName);

    List<Order> searchByPriceGreaterThan(double price);

    List<Order> searchByQuantityGreaterThanQueryDSL(int quantityMin);

    List<Order> searchByQuantityGreaterThanCriteria(int quantityMin);
}
