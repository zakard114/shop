package com.heeju.shop.service;

import com.heeju.shop.entity.ItemImg;
import com.heeju.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.lang.management.ManagementFactory;

@Service
@RequiredArgsConstructor
public class ItemImgService {

    @Value("${itemImgLocation}")    // Retrieve the itemLocation value registered in application.properties through
    // @Value and set it to the itemImgLocation variable.
    private String itemImgLocation;
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;  // FileService.java

    public void  saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        // MultipartFile: When implementing file upload, if the client is a web browser, the file is registered and
        // transmitted through the form. At this time, the "Content-Type" attribute of the HTTP method sent by the web browser
        // is set to "multipart/form-data", and the message is encoded and transmitted according to the specified format.
        // To process this, the server separates each part of the multipart message and obtains information about the individual files.
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // file upload
        if(!StringUtils.isEmpty(oriImgName)) {
            // StringUtils: Apache common library that provides useful functions when processing strings.
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // If the user has registered a product image: Call the FileService.uploadFile method with the path to save,
            // the name of the file, and the byte array of the file as the file upload parameters. This "return String
            // savedFileName = uuid.toString() + extension", and stores this (the name of the locally saved file)
            // in the imgName variable.
            imgUrl = "/images/item/"+imgName;   
        }

        // Save product image information
        itemImg.updateItemImg(oriImgName, imgName, imgUrl); // #1
        itemImgRepository.save(itemImg); // #2

        // #4,5: Save the entered product image information.
        // imgName: Name of the product image file actually stored locally
        // oriImgName: The original name of the uploaded product image file
        // imgUrl: Path to load the locally saved product image file as a result of upload. For this part, refer to WebMvcConfig.java.
    }
}
