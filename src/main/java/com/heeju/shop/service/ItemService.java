package com.heeju.shop.service;

import com.heeju.shop.dto.ItemFormDto;
import com.heeju.shop.entity.Item;
import com.heeju.shop.entity.ItemImg;
import com.heeju.shop.repository.ItemImgRepository;
import com.heeju.shop.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;


    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // Product registration
        Item item = itemFormDto.createItem(); // Create an item object using the data entered from the product registration form.
        itemRepository.save(item);            // Save product data.
        // Register image
        for (int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0) // If it is the first image, set the value of whether it is a representative product image to "Y".
                       // Set the remaining product images to “N”.
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // Save product image information.
        }

        return item.getId();
    }
}
