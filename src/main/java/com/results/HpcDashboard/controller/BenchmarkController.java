package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.Benchmark;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.BenchmarkRepo;
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
public class BenchmarkController {
    @Autowired
    BenchmarkRepo benchmarkRepo;

    @GetMapping("/benchmark")
    public String showBenchmark() {
        return "benchmark";
    }

}
