package com.results.HpcDashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;
@Controller
public class HelperController {

    @GetMapping("/reports")
    public String showReports() {
        return "reports";
    }

    @GetMapping("/userList")
    public String showUsers() {
        return "userList";
    }

}

