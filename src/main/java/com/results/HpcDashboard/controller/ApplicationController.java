package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;


    @GetMapping("/application")
    public String showApplication() {
        return "application";
    }


}
