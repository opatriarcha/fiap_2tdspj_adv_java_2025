package br.com.empresa.springCrud.AppExample.domainmodel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name="ORDER_ITEMS")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderItem {
    @EmbeddedId
    @Getter
    @Setter
    private OrderItemKey id;

    @Getter @Setter
    private String itemName;

    @Getter @Setter
    private int quantity;

    @ManyToOne()
//    @MapsId("orderId")
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false),
            @JoinColumn(name = "order_id", referencedColumnName = "order_id",insertable = false, updatable = false)
    })
    @Getter @Setter
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}