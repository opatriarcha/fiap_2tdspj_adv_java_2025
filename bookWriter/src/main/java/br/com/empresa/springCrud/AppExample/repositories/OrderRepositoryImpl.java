package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.Order;
import br.com.empresa.springCrud.AppExample.domainmodel.QOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.dsl.BooleanExpression;


import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> findByQuantityGreaterThanQueryDsl(int minQuantity) {
        QOrder qOrder = QOrder.order;
        BooleanExpression predicate = qOrder.quantity.gt(minQuantity);
        // A partir da versão que vamos implementar: temos que usar EntityManager
        var jpaQueryFactory = new com.querydsl.jpa.impl.JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectFrom(qOrder)
                .where(predicate)
                .fetch();

    }

    @Override
    public List<Order> findByQuantityGreaterThanCriteria(int minQuantity) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> orderRoot = cq.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.greaterThan(orderRoot.get("quantity"), minQuantity));

        cq.select(orderRoot).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
