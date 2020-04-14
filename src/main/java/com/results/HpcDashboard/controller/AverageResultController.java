package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.repo.AverageResultRepo;
import com.results.HpcDashboard.services.AverageResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/avg")
public class AverageResultController {

    @Autowired
    AverageResultRepo averageResultRepo;

    @Autowired
    AverageResultService averageResultService;

    @GetMapping("/result")
    public List<AverageResult> getAvgResult(){
        List<AverageResult> list = null;
        list = averageResultService.getAverageResult();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }

//    @GetMapping("/resultSingle")
//    public AverageResult getSingleAvgResult(String cpu_sku, String bm_name, int nodes){
//        AverageResult list = null;
//        list = averageResultService.getSingleAvgResult(bm_name,cpu_sku,nodes);
//        if(list ==null){
//            return null;
//        }
//        return list;
//    }


//    @PostMapping("/result")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity insertAvgResult(@RequestBody AverageResult averageResult){
//        if (averageResult.getCpu_sku() == "" || averageResult.getCpu_sku().equals(null) || averageResult.getBm_name() == "" || averageResult.getBm_name().equals(null) )
//            return null;
//        averageResultService.insertAverage(averageResult);
//        return ResponseEntity.ok().build();
//    }


//    @PutMapping("/result/{bm_name}/{cpu_sku}/{nodes}/{avg}")
//    public ResponseEntity updateBook(@PathVariable("bm_name") String bm_name, @PathVariable("cpu_sku") String cpu_sku, @PathVariable("nodes") int nodes, @PathVariable("avg") double avg) {
//        averageResultService.updateAverage(cpu_sku, nodes, bm_name,avg);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping(value = "/result/{bm_name}/{cpu_sku}/{nodes}")
//    public ResponseEntity deleteAverage(@PathVariable("bm_name") String bm_name, @PathVariable("cpu_sku") String cpu_sku, @PathVariable("nodes") int nodes) {
//        averageResultService.deleteAverage(cpu_sku, nodes, bm_name);
//        return ResponseEntity.ok().build();
//    }


}
