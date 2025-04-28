package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.Order;
import br.com.empresa.springCrud.AppExample.domainmodel.OrderKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, OrderKey>,
        QuerydslPredicateExecutor<Order>,
        OrderRepositoryCustom {

    // MethodNameQuery: Busca pelo nome do produto
    List<Order> findByProductNameContainingIgnoreCase(String productName);

    // JPQL personalizado: Busca pelo preço
    @org.springframework.data.jpa.repository.Query("SELECT o FROM Order o WHERE o.price >= :price")
    List<Order> findOrdersWithPriceGreaterThanEqual(double price);
}
