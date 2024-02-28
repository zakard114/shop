package com.heeju.shop.service;

import com.heeju.shop.constant.ItemSellStatus;
import com.heeju.shop.dto.ItemFormDto;
import com.heeju.shop.entity.Item;
import com.heeju.shop.entity.ItemImg;
import com.heeju.shop.repository.ItemImgRepository;
import com.heeju.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemImgRepository itemImgRepository;


    List<MultipartFile> createMultipartFiles() throws Exception {
        // This is a method that creates and returns a fake MultipartFile list using the MockMultipartFile class.

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5 ; i++) {
            String path = "D:/IT_SPACES/AutoProject/SpringBoot/Sources/shop/item";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    //public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
    @Test
    @DisplayName("Product Registration Test")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void saveItem() throws Exception {
        ItemFormDto itemFormDto = new ItemFormDto(); // Set product data input from the product registration screen
        itemFormDto.setItemNm("Test Product");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("This is a test product.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
        // Pass product data and image information as parameters, save them, and return the saved product ID value as the return value.

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        // Check whether the entered product data and the actually saved product data are the same.
        assertEquals(itemFormDto.getItemSellStatus(),
                item.getItemSellStatus());

        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(),
                itemImgList.get(0).getOriImgName()); // Check if the product image is the same as the original image file name of the first file.

    }


}