package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKey>,
        QuerydslPredicateExecutor<OrderItem>,
        OrderItemRepositoryCustom {

    List<OrderItem> findByItemNameContainingIgnoreCase(String itemName);
}
