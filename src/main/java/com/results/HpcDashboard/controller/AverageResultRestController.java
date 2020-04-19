package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.CPUDto;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.repo.AverageResultRepo;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.services.CPUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/avg")
public class AverageResultRestController {

    @Autowired
    AverageResultRepo averageResultRepo;

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    CPUService cpuService;


    @GetMapping("/result")
    public List<AverageResult> getAvgResult(){
        List<AverageResult> list = null;
        list = averageResultService.getAverageResult();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }


    @GetMapping("/result/{cpu}/{app_name}")
    public List<AverageResult> getAvgResultCPU(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name){
        List<AverageResult> list = null;
        list = averageResultService.getAvgResultCPUApp(cpu,app_name);
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }

    @GetMapping("/cpus")
    public List<CPUDto> getAllCPUsCores(){
        List<CPUDto> list = null;
        list = cpuService.getAllCPUsCores();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }


}
