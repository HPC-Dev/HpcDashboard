package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.ChartsResponse;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.services.ApplicationService;
import com.results.HpcDashboard.services.AverageResultService;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/chart")
public class ChartRestController {

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    ApplicationService applicationService;

    static Map<String,String> appMap;

    public static String getAppName(String app){
        appMap = new HashMap<>();
        appMap.put("openfoam","OpenFOAM");
        appMap.put("namd","NAMD");
        return appMap.getOrDefault(app,app);
    }

    @GetMapping("/resultApp/{cpu}/{app_name}/{node}")
    public List<ChartsResponse> getChartResult(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("node") int node){
        List<ChartsResponse> listResponse = new ArrayList<>();
        ChartsResponse chartsResponse = null;
        List<AverageResult> list = null;
        list = averageResultService.getAvgResultCPUAppNode(cpu,app_name,node);
        String appCpu = getAppName(app_name)+" - "+cpu;
        String metric = "";
        if(applicationService.getMetric(app_name).length() > 0 ) {
            metric = WordUtils.capitalize(applicationService.getMetric(app_name));
        }
        List<String> label = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for(AverageResult avgRes : list){
            label.add(avgRes.getBmName());
            data.add(avgRes.getAvgResult());
        }
        chartsResponse = ChartsResponse.builder().metric(metric).appCPUName(appCpu).dataset(data).labels(label).build();
        listResponse.add(chartsResponse);
        return listResponse;
    }



    @GetMapping("/resultBm/{cpu}/{app_name}/{bm_name}")
    public List<ChartsResponse> getAvgResultCPUBM(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("bm_name") String bm_name){
        List<ChartsResponse> listResponse = new ArrayList<>();
        ChartsResponse chartsResponse = null;
        List<AverageResult> list = null;
        list = averageResultService.getAvgResultCPUAppBm(cpu,app_name,bm_name);

        String appBmCpu = getAppName(app_name)+" - "+bm_name+" - "+cpu;
        String metric = WordUtils.capitalize(applicationService.getMetric(app_name));
        List<String> label = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for(AverageResult avgRes : list){
            label.add(avgRes.getNodes()+" Node");
            data.add(avgRes.getAvgResult());
        }
        chartsResponse = ChartsResponse.builder().metric(metric).appCPUName(appBmCpu).dataset(data).labels(label).build();
        listResponse.add(chartsResponse);
        return listResponse;

    }
}