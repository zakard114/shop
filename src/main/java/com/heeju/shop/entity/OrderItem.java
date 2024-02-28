package com.heeju.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // Since one product can be included in multiple ordered products,
                        // set up a many-to-one one-way mapping based on the ordered products.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;    // Since multiple products can be ordered in one order, first set up a many-to-one one-way
                            // mapping between the order product entity and the order entity.
    private int orderPrice; // Order price
    private int count;  // Quantity

}
