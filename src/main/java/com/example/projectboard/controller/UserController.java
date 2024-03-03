package com.example.projectboard.controller;

import com.example.projectboard.domain.UserAccount;
import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.dto.request.SignupRequestDto;
import com.example.projectboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDto signupRequestDto, ModelMap map){
        UserAccount userAccount = userService.signUp(signupRequestDto);
        map.addAttribute("userAccount",userAccount);
        return "redirect:/articles";
    }

}
