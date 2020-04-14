package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.CPUDto;
import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.services.CPUService;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    ResultRepo resultRepo;

    @Autowired
    Util util;

    @Autowired
    CPUService cpuService;

    @GetMapping("/cpu")
    public Map<String,Integer> listCPU(){
       return  cpuService.getAllCPUs();
    }


//    @GetMapping("/results/{bm_name}/{cpu}/{nodes}")
//    public List<Double> getRes(@PathVariable("bm_name") String bm_name, @PathVariable("cpu") String cpu, @PathVariable("nodes") int nodes){
//        List<Double> resultList = resultRepo.findresultsByAppCPU(bm_name,cpu,nodes);
//
//        Double total = 0.0;
//        int len = resultList.size();
//        for(Double d : resultList){
//          total+=d;
//        }
//        double average = total/len;
//        System.out.println(util.round(average,2));
//        return resultList;
//    }



}
