package com.heeju.shop.entity;

import com.heeju.shop.OrderStatus.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // Specifies "order" as the table mapped to the Order entity because there is an "order" keyword used when sorting.
@Getter @Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ManyToOne is based on Order
    @JoinColumn(name = "member_id")
    private Member member;  // Since one member can place multiple orders, many-to-one one-way mapping
                            // is performed based on the order entity.
    private LocalDateTime orderDate;    // Order date
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // Order status

    // CasecadeType.All: CascadeTypeAll option that transfers all changes in the persistence state of the parent entity
    // to child entities.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // One-to-many mapping with the order product entity. Since order_id, a foreign key, is in the order_item table,
    // the owner of the relationship is the OrderItem entity. Since the Order entity is not the owner, set the owner of
    // the association using the “mappedBy” property. The reason for writing "order" as the property value can be
    // interpreted to mean that it is managed by ORder in OrderItem. In other words, you can set Order, a field of OrderItem,
    // which is the owner of the association, as the value of mappedBy.
    private List<OrderItem> orderItems = new ArrayList<>(); // Since one order has multiple ordered products, mapping is
                                                            // done using the List data type.

}
