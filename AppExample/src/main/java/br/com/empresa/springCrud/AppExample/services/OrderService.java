package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Order;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderId;
import br.com.empresa.springCrud.AppExample.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(OrderId id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void deleteById(OrderId id) {
        orderRepository.deleteById(id);
    }

    public Order partialUpdate(OrderId id, Map<String, Object> updates) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "productName":
                        order.setProductName((String) value);
                        break;
                    case "quantity":
                        order.setQuantity((Integer) value);
                        break;
                    case "price":
                        order.setPrice((Double) value);
                        break;
                }
            });
            return orderRepository.save(order);
        }
        return null; // Ou lançar uma exceção
    }
}