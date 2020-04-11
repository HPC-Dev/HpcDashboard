package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.services.ApplicationService;
import com.results.HpcDashboard.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.math.BigDecimal;


@Controller
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;


    @GetMapping("/application")
    public String showApplication() {
        return "application";
    }


}
