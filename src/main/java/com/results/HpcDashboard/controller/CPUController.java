package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.CPURepo;
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
    CPURepo cpuRepo;


    @GetMapping("/cpu")
    public String showCPU() {
        return "cpu";
    }


    @GetMapping("/cpuInsert")
    public String showCPU1() {
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
            CPU cpu = CPU.builder().cpu_generation(resultData[1]).max_ddr_freq(resultData[9]).ddr_channels(Integer.valueOf(resultData[8])).l3_cache(Integer.valueOf(resultData[7])).peak_freq(resultData[6]).base_freq(resultData[5]).cores(Integer.valueOf(resultData[4])).tdp(resultData[3]).cpu_sku(resultData[2]).cpu_manufacturer(resultData[0]).build();
            cpuRepo.save(cpu);
        }
            return "redirect:/cpuInsert?success";

        }
    }

