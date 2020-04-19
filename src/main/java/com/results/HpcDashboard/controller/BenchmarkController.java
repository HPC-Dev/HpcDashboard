package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.repo.BenchmarkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BenchmarkController {
    @Autowired
    BenchmarkRepo benchmarkRepo;

    @GetMapping("/benchmark")
    public String showBenchmark() {
        return "benchmark";
    }

}
