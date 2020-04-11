package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    BenchmarkRepo benchmarkRepo;

    @Autowired
    CPURepo cpuRepo;

    @Autowired
    ResultRepo resultRepo;

    @Autowired
    Util util;

    @PersistenceContext
    private EntityManager entityManager;

//    @GetMapping("/applications")
//    public List<Application> getApps(){
//        return applicationRepo.findAll();
//    }
//
//    @GetMapping("/cpus")
//    public List<CPU> getCPU(){
//        return cpuRepo.findAll();
//    }
//
//    @GetMapping("/results/{bm_name}")
//    public List<BigDecimal> getRes(@PathVariable("bm_name") String bm_name){
//        List<BigDecimal> resultList = resultRepo.findresults(bm_name);
//
//
//        return resultList;
//    }
//
//    @GetMapping("/benchmarks")
//    public List<BenchmarkDto> getBenchmarks(){
//        List<BenchmarkDto> benchmarkList = null;
//        benchmarkList = util.getBenchmarks(entityManager);
//        if (benchmarkList == null || benchmarkList.size() == 0)
//            return Collections.emptyList();
//
//        return benchmarkList;
//    }

}
