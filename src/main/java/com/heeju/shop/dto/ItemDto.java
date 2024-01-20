package com.heeju.shop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemDto {

    private Long id;

    private String itemNm;

    private Integer price;

    private String itemDetail;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
