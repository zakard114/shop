package com.heeju.shop.entity;

import com.heeju.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "item") // Maps with DB’s item table
@AllArgsConstructor
@NoArgsConstructor
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    // product code

    @Column(nullable = false, length = 50)
    private String itemNm;  // product name

    @Column(name = "price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

}
