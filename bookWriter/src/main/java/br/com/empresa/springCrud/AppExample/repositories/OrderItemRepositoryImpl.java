package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;
import br.com.empresa.springCrud.AppExample.domainmodel.QOrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrderItem> findByQuantityGreaterThanQueryDsl(int quantityMin) {
        QOrderItem orderItem = QOrderItem.orderItem;
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        return factory.selectFrom(orderItem)
                .where(orderItem.quantity.gt(quantityMin))
                .fetch();
    }

    @Override
    public List<OrderItem> findByQuantityGreaterThanCriteria(int quantityMin) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderItem> cq = cb.createQuery(OrderItem.class);
        Root<OrderItem> root = cq.from(OrderItem.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.greaterThan(root.get("quantity"), quantityMin));

        cq.select(root).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
