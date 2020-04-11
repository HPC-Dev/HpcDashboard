package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.services.ResultService;
import com.results.HpcDashboard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;


@Controller
public class WebController {

    @Autowired
    UserService userService;

    @RequestMapping({ "/", "/index" })
    public String index(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if(user!=null){
            model.addAttribute("user", user.getFirstName());
        }
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
     return "login";
    }

    @GetMapping("/charts")
    public String showCharts(Model model) {
        return "charts";
    }

}
