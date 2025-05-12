package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Order;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderKey;
import br.com.empresa.springCrud.AppExample.domainmodel.QOrder;
import br.com.empresa.springCrud.AppExample.domainmodel.QOrderItem;
import br.com.empresa.springCrud.AppExample.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepositoryImpl;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepositoryImpl) {
        this.orderRepositoryImpl = orderRepositoryImpl;
    }

    @Override
    public List<Order> findAll() {
        return orderRepositoryImpl.findAll();
    }

    @Override
    public Page<Order> findAllPaged(Pageable pageable) {
        return orderRepositoryImpl.findAll(pageable);
    }

    @Override
    public Optional<Order> findById(OrderKey id) {
        return orderRepositoryImpl.findById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepositoryImpl.save(order);
    }

    @Override
    public void deleteById(OrderKey id) {
        orderRepositoryImpl.deleteById(id);
    }

    @Override
    public Order partialUpdate(OrderKey id, Map<String, Object> updates) {
        Optional<Order> optionalOrder = orderRepositoryImpl.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
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
            return orderRepositoryImpl.save(order);
        }
        return null;
    }

    @Override
    public boolean existsById(OrderKey id) {
        return orderRepositoryImpl.existsById(id);
    }

    @Override
    public List<Order> searchByProductName(String productName) {
        return orderRepositoryImpl.findByProductNameContainingIgnoreCase(productName);
    }

    @Override
    public List<Order> searchByPriceGreaterThan(double price) {
        return orderRepositoryImpl.findOrdersWithPriceGreaterThanEqual(price);
    }

    @Override
    public List<Order> searchByQuantityGreaterThanQueryDSL(int quantityMin) {
        return orderRepositoryImpl.findByQuantityGreaterThanQueryDsl(quantityMin);
    }

    @Override
    public List<Order> searchByQuantityGreaterThanCriteria(int quantityMin) {
        return orderRepositoryImpl.findByQuantityGreaterThanCriteria(quantityMin);
    }

    public void qqermerda(){
        QOrderItem orderItem = QOrderItem.orderItem;

    }
}
