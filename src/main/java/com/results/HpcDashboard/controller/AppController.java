package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Applications;
import com.results.HpcDashboard.models.Benchmarks;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.repo.ApplicationsRepo;
import com.results.HpcDashboard.repo.BenchmarksRepo;
import com.results.HpcDashboard.repo.CPURepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    ApplicationsRepo applicationsRepo;

    @Autowired
    BenchmarksRepo benchmarksRepo;

    @Autowired
    CPURepo cpuRepo;

    @GetMapping("/applications")
    public List<Applications> getApps(){
        return applicationsRepo.findAll();
    }

    @GetMapping("/benchmarks")
    public List<Benchmarks> getBM(){
        return benchmarksRepo.findAll();
    }

    @GetMapping("/cpu")
    public List<CPU> getCPU(){
        return cpuRepo.findAll();
    }
}
