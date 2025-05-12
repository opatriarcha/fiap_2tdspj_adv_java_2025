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

        //Instancio o QOrderItem ou qualquer classe que comece com Q.
        QOrderItem orderItem = QOrderItem.orderItem;

        //CRIANDO JPAQueryFactory a partir do PersistenceContext.
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);

        //criando a query aqui
        return factory.selectFrom(orderItem)
                .where(
                        orderItem.quantity.gt(quantityMin)
                                .and(orderItem.quantity.loe(10000))
                )
                .fetch();


    }

    @Override
    public List<OrderItem> findByQuantityGreaterThanCriteria(int quantityMin) {
        // CRIO O CRITERIA BUILDER
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        //CRIO O CRITERIA QUERY
        CriteriaQuery<OrderItem> query = cb.createQuery(OrderItem.class);

        //QUAL A CLAUSULA FROM?
        Root<OrderItem> root = query.from(OrderItem.class);

        //CRIA A LISTA DE PREDICADOS
        List<Predicate> predicates = new ArrayList<>();

        //ADICIONA NOVO PREDICADO NA LISTA
        predicates.add(cb.greaterThan(root.get("quantity"), quantityMin));

        //DEFINO A SELECAO
        query.select(root).where(predicates.toArray(new Predicate[0]));

        //EXECUTO A QUERY RETORNANDO O RESULTSETLIST
        return entityManager.createQuery(query).getResultList();
    }
}
