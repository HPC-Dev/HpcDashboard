package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.UserRegistrationDto;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {
    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        User existingEmail = userService.findByEmail(userDto.getEmail());
        if (existingEmail != null) {
            result.rejectValue("email", null, "There is already an account registered with this email");
        }
        User existingUserName = userService.findByUserName(userDto.getUserName());
        if (existingUserName != null) {
            result.rejectValue("userName", null, "There is already an account registered with this username");
        }

        if (result.hasErrors()) {
            return "register";
        }
        userService.save(userDto);
        return "redirect:/register?success";
    }
}
