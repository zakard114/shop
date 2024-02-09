package com.heeju.shop.controller;

import com.heeju.shop.dto.MemberFormDto;
import com.heeju.shop.entity.Member;
import com.heeju.shop.service.MemberService;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping(value = "/")
    public String main(){
        return "main";
    }

}
