package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderItemKey;
import br.com.empresa.springCrud.AppExample.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public Page<OrderItem> findAllPaged(Pageable pageable) {
        return orderItemRepository.findAll(pageable);
    }

    @Override
    public Optional<OrderItem> findById(OrderItemKey id) {
        return orderItemRepository.findById(id);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteById(OrderItemKey id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public OrderItem partialUpdate(OrderItemKey id, Map<String, Object> updates) {
        Optional<OrderItem> optional = orderItemRepository.findById(id);
        if (optional.isPresent()) {
            OrderItem orderItem = optional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "itemName":
                        orderItem.setItemName((String) value);
                        break;
                    case "quantity":
                        orderItem.setQuantity((Integer) value);
                        break;
                }
            });
            return orderItemRepository.save(orderItem);
        }
        return null;
    }

    @Override
    public boolean existsById(OrderItemKey id) {
        return orderItemRepository.existsById(id);
    }

    @Override
    public List<OrderItem> searchByItemName(String itemName) {
        return orderItemRepository.findByItemNameContainingIgnoreCase(itemName);
    }

    @Override
    public List<OrderItem> searchByQuantityGreaterThanQueryDSL(int quantityMin) {
        return orderItemRepository.findByQuantityGreaterThanQueryDsl(quantityMin);
    }

    @Override
    public List<OrderItem> searchByQuantityGreaterThanCriteria(int quantityMin) {
        return orderItemRepository.findByQuantityGreaterThanCriteria(quantityMin);
    }
}
