package com.heeju.shop.dto;

import com.heeju.shop.constant.ItemSellStatus;
import com.heeju.shop.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "Product name is a required input value.")
    private String itemNm;
    @NotNull(message = "Price is required.")
    private Integer price;
    @NotBlank(message = "Name is required.")
    private String itemDetail;
    @NotNull(message = "Inventory is required.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    // This is a list that saves product image information when editing a product after saving it.
    private List<Long> itemImgIds = new ArrayList<>();
    // This is a list that stores the image ID of the product. When registering a product, since the product image has
    // not yet been saved, no value is entered and it is used to store the image ID when editing.
    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class); // #1
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class); // #2
    }

    // #1, #2: This is a method that copies data between an entity object and a DTO object using modelMapper and returns the copied object.

}
