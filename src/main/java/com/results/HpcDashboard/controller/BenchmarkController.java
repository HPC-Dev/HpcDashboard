package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.Benchmark;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.services.BenchmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class BenchmarkController {
    @Autowired
    BenchmarkRepo benchmarkRepo;

    @Autowired
    BenchmarkService benchmarkService;

    @GetMapping("/benchmark")
    public String showBenchmark() {
        return "benchmark";
    }

    @PostMapping(value = "/bmJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertBmJson(@RequestBody List<Benchmark> benchmarks) {
        if(benchmarks != null || benchmarks.size() > 0 )
            benchmarkService.insertBmCsv(benchmarks);

        return new ResponseEntity("Success!",HttpStatus.OK);
    }

}
