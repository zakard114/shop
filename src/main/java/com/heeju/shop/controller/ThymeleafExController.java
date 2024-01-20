package com.heeju.shop.controller;

import com.heeju.shop.dto.ItemDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/thymeleaf")
public class ThymeleafExController {

    @GetMapping(value="/ex01")
    public String thymeleafExample01(Model model){
        String str = "yes";
        model.addAttribute("obj", str);
        return "thymeleafEx/ex01";
    }

    @GetMapping(value="/ex02")
    public ModelAndView thymeleafExample02(){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("Product detail");
        itemDto.setItemNm("Test Product1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        return new ModelAndView("/thymeleafEx/thymeleafEx02","itemDto", itemDto);
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1;i<=10;i++){

            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("Product detail"+i);
            itemDto.setItemNm("Test Product"+i);
            itemDto.setPrice(10000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <=10 ; i++) {

            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("Product detail"+i);
            itemDto.setItemNm("Test Product"+i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }
}
