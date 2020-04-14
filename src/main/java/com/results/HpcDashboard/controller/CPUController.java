package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.services.CPUService;
import com.results.HpcDashboard.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;


@Controller
public class CPUController {
    @Autowired
    CPUService cpuService;


    @GetMapping("/cpu")
    public String showCPU() {
        return "cpu";
    }


    @GetMapping("/cpuInsert")
    public String showCPUForm() {
        return "cpuInsert";
    }


    @PostMapping("/cpu")
        public String insertCPU(@RequestParam("data") String data){
        String[] resultReturned = data.split("!");
        for(String individualResult : resultReturned){
            String[] resultData = individualResult.split(",");
            if(resultData.length != 10)
            {
                return "redirect:/cpuInsert?failure";
            }

            cpuService.insertCPU(resultData);
        }
            return "redirect:/cpuInsert?success";

        }
    }

