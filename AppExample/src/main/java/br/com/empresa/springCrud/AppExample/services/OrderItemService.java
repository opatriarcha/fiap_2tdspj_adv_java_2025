package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderItemId;
import br.com.empresa.springCrud.AppExample.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItem> findById(OrderItemId id) {
        return orderItemRepository.findById(id);
    }

    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public void deleteById(OrderItemId id) {
        orderItemRepository.deleteById(id);
    }

    public OrderItem partialUpdate(OrderItemId id, Map<String, Object> updates) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        if (orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();
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
        return null; // Ou lançar uma exceção
    }
}