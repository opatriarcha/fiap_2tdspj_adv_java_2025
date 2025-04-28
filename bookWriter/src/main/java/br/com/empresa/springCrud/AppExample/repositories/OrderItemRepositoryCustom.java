package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;

import java.util.List;

public interface OrderItemRepositoryCustom {

    List<OrderItem> findByQuantityGreaterThanQueryDsl(int minQuantity);

    List<OrderItem> findByQuantityGreaterThanCriteria(int minQuantity);
}
