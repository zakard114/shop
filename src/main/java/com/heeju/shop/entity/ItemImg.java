package com.heeju.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_img")
@Getter @Setter
public class ItemImg extends BaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imgName; // image file name
    private String oriImgName; // Original image file name
    private String imgUrl;  // Image viewing path
    private String repimgYn; // Whether a representative image

    @ManyToOne(fetch = FetchType.LAZY)  // Mapping the product entity to a many-to-one one-way relationship.
    // Set up lazy loading to query data when mapped product entity information is needed.
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        // Method that updates image information by receiving the original image file name, image file name to be updated, and image path as parameters.
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }



}
