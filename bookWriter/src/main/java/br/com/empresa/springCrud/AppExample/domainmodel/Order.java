package br.com.empresa.springCrud.AppExample.domainmodel;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name="ORDERS")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order {
    @EmbeddedId
//    @AttributeOverrides({
//            @AttributeOverride(name = "userId", column = @Column(name = "user_id")),
//            @AttributeOverride(name = "orderId", column = @Column(name = "order_id"))
//    })
    @Getter @Setter
    private OrderKey id;

    @Getter @Setter
    private String productName;

    @Getter @Setter
    private int quantity;

    @Getter @Setter
    private double price;

    @ManyToOne
//    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @Getter @Setter
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter @Setter
    private Set<OrderItem> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}