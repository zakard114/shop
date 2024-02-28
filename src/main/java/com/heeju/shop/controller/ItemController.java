package com.heeju.shop.controller;

import com.heeju.shop.dto.ItemFormDto;
import com.heeju.shop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

////    ModelAndView version
//    @GetMapping(value = "/admin/item/new")
//    public ModelAndView itemForm(){
//        ItemFormDto itemForm = new ItemFormDto());
//        return new ModelAndView("/item/itemForm", "itemForm", ifd);
//    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList) {

        if(bindingResult.hasErrors()) { // If there are no required values when registering a product, switch back to the product registration page.
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            // If there is no first image when registering a product, it switches to the product registration page with
            // an error message. The first image of the product will be specified as a required value to be used as the
            // product image to be displayed on the main page.
            model.addAttribute("errorMessage", "The first product image is required.");
            return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto, itemImgFileList); // Call product storage logic. Pass itemImgFileList
                                                                // containing product information and product image information as a parameter.
        } catch (Exception e){
            model.addAttribute("errorMessage", "An error occurred while registering the product.");
            return "item/itemForm";
        }

        return "redirect:/";   // If the product has been registered properly, move to the main page.
    }


}
