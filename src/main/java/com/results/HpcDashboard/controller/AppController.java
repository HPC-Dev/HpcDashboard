package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.CPUDto;
import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class AppController {

    @Autowired
    UserService userService;

    @Autowired
    AverageResultService averageResultService;

    @ModelAttribute("command")
    public FormCommand formCommand() {
        return new FormCommand();
    }

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

    @GetMapping("/data")
    public String showData(Model model) {

        List<String> cpu_list = averageResultService.getCpu();
        List<String> app_list = averageResultService.getApp();
        model.addAttribute("cpus", cpu_list );
        model.addAttribute("apps", app_list );
        return "result";
    }
}
