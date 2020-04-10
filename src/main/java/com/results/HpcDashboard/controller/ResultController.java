package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;


@Controller
public class ResultController {
    @Autowired
    ResultService resultService;

    @GetMapping("/result")
    public String showResultForm(Model model) {
        return "resultForm";
    }

    @PostMapping("/result")
    public String insertResult(@RequestParam("data") String data){

        String[] resultData = data.split(",");
        if(resultData.length != 8)
        {
            return "redirect:/result?failure";
        }
        Result result = new Result(resultData[0],resultData[1],resultData[2],Integer.valueOf(resultData[3]),Integer.valueOf(resultData[4]),resultData[5], new BigDecimal(resultData[6]),resultData[7].toUpperCase());
        resultService.insertResult(result);
        return "redirect:/result?success";

    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("results", resultService.getAllResults());
        return "resultDashboard";
    }

}
