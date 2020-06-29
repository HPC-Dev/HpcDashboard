package com.results.HpcDashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

@Controller
public class AppErrorController implements ErrorController {
 
//    @RequestMapping("/error")
//    public String handleError() {
//        return "error";
//    }
 
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
