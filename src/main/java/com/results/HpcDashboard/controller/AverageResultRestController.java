package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.CPUDto;
import com.results.HpcDashboard.dto.partComparision.CompareResult;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.repo.AverageResultRepo;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.services.CPUService;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/avg")
public class AverageResultRestController {

    @Autowired
    AverageResultRepo averageResultRepo;

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    CPUService cpuService;

    @Autowired
    Util util;


    public String getLowerHigher(String app){
        HashMap<String,String> appMap = util.getAppMap();
        return appMap.getOrDefault(app,app);
    }


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

    @GetMapping("/result/{cpu}/{app_name}/{bm_name}")
    public List<AverageResult> getAvgResultCPUBM(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("bm_name") String bm_name){
        List<AverageResult> list = null;
        list = averageResultService.getAvgResultCPUAppBm(cpu,app_name,bm_name);
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }


//    @GetMapping("/cpus")
//    public List<CPUDto> getAllCPUsCores(){
//        List<CPUDto> list = null;
//        list = cpuService.getAllCPUsCores();
//        if(list ==null){
//            return Collections.emptyList();
//        }
//        return list;
//    }

    @GetMapping("/resultComparision/{app_name}/{cpu1}/{cpu2}")
    public CompareResult getAvgBySelectedCPU(@PathVariable("app_name") String app_name, @PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2) {

        CompareResult compareResult = null;
        String comment = null;
        List<AverageResult> list1 = averageResultService.getCompDataBySelectedCPU(app_name, cpu1);
        List<AverageResult> list2 = averageResultService.getCompDataBySelectedCPU(app_name, cpu2);

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0)
            return compareResult;


        Set<String> bms = new LinkedHashSet<>();
        bms.add("");

        if (list2.size() > list1.size()){
            for (AverageResult avg : list1) {
                bms.add(avg.getBmName());
            }
        }
        else{
            for (AverageResult avg : list2) {
                bms.add(avg.getBmName());
            }
        }

        Map<String,Double> result1 = new LinkedHashMap<>();
        Map<String,Double> result2 = new LinkedHashMap<>();

        Map<String,String> perfDifference = new LinkedHashMap<>();

        for(AverageResult a : list1){
            result1.put(a.getBmName(),a.getAvgResult());
        }
        for(AverageResult a : list2){
            result2.put(a.getBmName(),a.getAvgResult());
        }

        perfDifference.put("","Perf diff(%)");

        Set<String> keys = null;

        if(result2.size() > result1.size())
         keys= result1.keySet();
        else
         keys= result2.keySet();


        for(String k : keys){

            double val1 = result1.getOrDefault(k,0.0);
            double val2 = result2.getOrDefault(k,0.0);

            if(val1 != 0.0 && val2 != 0.0 ) {

                if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                    if (Double.compare(val1, val2) < 0) {
                        double d = (val2 - val1) / Math.abs(val1);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "+" + percentage + "%");
                    } else if (Double.compare(val1, val2) > 0) {
                        double d = (val1 - val2) / Math.abs(val2);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "-" + percentage + "%");
                    } else {
                        perfDifference.put(k, 0 + "%");
                    }
                    comment = "Higher is better";
                } else {
                    if (Double.compare(val1, val2) > 0) {
                        double d = (val1 - val2) / Math.abs(val2);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "+" + percentage + "%");
                    } else if (Double.compare(val1, val2) < 0) {
                        double d = (val2 - val1) / Math.abs(val1);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "-" + percentage + "%");
                    } else {
                        perfDifference.put(k, 0 + "%");
                    }
                    comment = "Lower is better";
                }
            }
        }

        Map<String,String> res1 = new LinkedHashMap<>();
        Map<String,String> res2 = new LinkedHashMap<>();

        res1.put("",cpu1);
        res2.put("",cpu2);

        for(String k : keys){
            res1.put(k,result1.getOrDefault(k,0.0).toString());
            res2.put(k,result2.getOrDefault(k,0.0).toString());
        }

        List<Map<String,String>> dataSets = new ArrayList<>();
        dataSets.add(res1);
        dataSets.add(res2);
        dataSets.add(perfDifference);

         compareResult = CompareResult.builder().appName(app_name).bmName(bms).resultData(dataSets).comment(comment).build();

        return compareResult;

    }

}
