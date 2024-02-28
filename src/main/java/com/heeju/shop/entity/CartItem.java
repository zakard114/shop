package com.heeju.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem extends BaseEntity{

    @Id
    @GeneratedValue // Default value is Strategy: GenerationType.AUTO
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // When the number of mapped entities is large, it is difficult for developers to
                                       // predict how queries will be executed in eager mode, and performance problems
                                       // occur because unused data is searched all at once. This is the need for LAZY loading mode.
    @JoinColumn(name="cart_id")
    private Cart cart;  // Since one shopping cart can contain multiple products, map it into a many-to-one relationship using the @ManyToOne annotation.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // Map the product entity to obtain information about the product to be added to the shopping cart.
                        // Since one product can be included as a shopping cart product in multiple shopping carts,
                        // it is mapped into a many-to-one relationship using the @ManyToOne annotation.

    private int count;  // Stores how many of the same product to put in the shopping cart.
}
