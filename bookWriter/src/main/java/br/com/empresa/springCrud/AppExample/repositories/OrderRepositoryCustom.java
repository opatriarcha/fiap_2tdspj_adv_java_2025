package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findByQuantityGreaterThanQueryDsl(int minQuantity);

    List<Order> findByQuantityGreaterThanCriteria(int minQuantity);
}
