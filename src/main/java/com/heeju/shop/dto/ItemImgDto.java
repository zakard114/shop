package com.heeju.shop.dto;

import com.heeju.shop.entity.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class ItemImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); // Add ModelMapper object as a member variable.
                                                                // Or @Autowired ModelMapper on the field.
                                                                // However, in that case, it cannot be used in the static method below.
    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class);
        // Receives the itemImg entity object as a parameter, and when the data type of the ItemImg object and the name of
        // the member variable are the same, copies the value to ItemImgDto and returns it. It can be called even without
        // creating an ItemImgDto object by declaring it as a static method.
        }
}
