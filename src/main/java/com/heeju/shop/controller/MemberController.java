package com.heeju.shop.controller;

import com.heeju.shop.dto.MemberFormDto;
import com.heeju.shop.entity.Member;
import com.heeju.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // Create a method in the MemberController class to move to the membership registration page.
    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto,
                            BindingResult bindingResult, Model model){

        // Declare @Valid in front of the object you want to verify,
        // and add bindingResult object as a parameter.
        // After inspection, the results are stored in bindingResult.

        // BindingResult
        //
        //Map was replaced with BindingResult.
        //This is an object that stores verification errors provided by Spring. - When a verification error occurs, it is stored here.
        //BindingResult The location of the bindingResult parameter must come after the @ModelAttribute Item item.
        //BindingResult is automatically included in the Model.
        //
        //Reason for use
        //
        //If you use BindingResult, the 400 error does not occur and the controller is called normally with the error. - In that case, you can process it before going to the error page.
        //Source: https://vprog1215.tistory.com/298

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            // If a duplicate member registration exception occurs
            // when registering as a member, an error message is delivered to the view.
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "Please check your ID or password");
        return "/member/memberLoginForm";
    }


}
