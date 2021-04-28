package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Processor;
import com.results.HpcDashboard.services.CPUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;


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


    @PostMapping(value = "/cpuJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertCPUJson(@RequestBody List<CPU> cpus) {
        if(cpus != null || cpus.size() > 0 )
            cpuService.insertCPUCsv(cpus);
        return new ResponseEntity("Success!",HttpStatus.OK);
    }


    @PostMapping(value = "/processorJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertProcessor(@RequestBody List<Processor> processors) {
        if(processors != null || processors.size() > 0 )
            cpuService.insertProcessorCSV(processors);
        return new ResponseEntity("Success!",HttpStatus.OK);
    }


    }

