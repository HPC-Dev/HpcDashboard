package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;


@Controller
public class ResultController {
    @Autowired
    ResultService resultService;

    @ModelAttribute("command")
    public FormCommand formCommand() {
        return new FormCommand();
    }

    @GetMapping("/result")
    public String showResultForm() {
        return "resultForm";
    }

    @PostMapping("/result")
    public String insertResult(
            @ModelAttribute("command") @Valid FormCommand command,
            Model model ) {

        String[] resultReturned = command.getTextareaField().split("!");
        for(String individualResult : resultReturned){
            String[] resultData = individualResult.split(",");
            if(resultData.length != 8)
            {
                return "redirect:/result?failure";
            }
            Result res = Result.builder().job_id(resultData[0]).app_name(resultData[1]).bm_name(resultData[2]).nodes(Integer.valueOf(resultData[3])).cores(Integer.valueOf(resultData[4])).node_name(resultData[5]).result(new BigDecimal(resultData[6])).cpu(resultData[7].toUpperCase()).build();
            resultService.insertResult(res);
        }
        return "redirect:/result?success";
    }


    @GetMapping("/dashboard")
    public String showDashboard() {
       return "resultDashboard";
    }

    @GetMapping("/application")
    public String showApplication() {
        return "application";
    }

    @GetMapping("/benchmark")
    public String showBenchmark() {
        return "benchmark";
    }

    @GetMapping("/cpu")
    public String showCPU() {
        return "cpu";
    }


}
