package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.HeatMap;
import com.results.HpcDashboard.repo.AverageResultRepo;
import com.results.HpcDashboard.repo.HeatMapRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class HeatMapService {

    @Autowired
    AverageResultRepo averageResultRepo;

    @Autowired
    HeatMapRepo heatMapRepo;

    @Transactional
    public void updateHeatResult(String cpu_sku, int nodes, String bm_name,double avg,double perCorePerf,double perfPerDollar,double perfPerWatt , int count, String runType) {
        if(cpu_sku == "" || cpu_sku.equals(null) || bm_name == "" || bm_name.equals(null))
            return;
        heatMapRepo.updateHeatResult(bm_name,cpu_sku,nodes,avg, perCorePerf,perfPerDollar,perfPerWatt, count, runType);
    }

    @Transactional
    public void deleteHeatResult(String cpu_sku, int nodes, String bm_name, String runType) {
        if(cpu_sku == "" || cpu_sku.equals(null) || bm_name == "" || bm_name.equals(null) )
            return;
        heatMapRepo.deleteHeatResult(bm_name,cpu_sku,nodes, runType);
    }

    @Transactional
    public void insertHeatResult(HeatMap heatResult) {
        if (heatResult== null || heatResult.getCpuSku() == "" || heatResult.getCpuSku().equals(null) || heatResult.getBmName() == "" || heatResult.getBmName().equals(null) )
            return;
        HeatMap heatMap = HeatMap.builder().category(heatResult.getCategory()).cores(heatResult.getCores()).perCorePerf(heatResult.getPerCorePerf()).isv(heatResult.getIsv()).appName(heatResult.getAppName().trim()).avgResult(heatResult.getAvgResult()).bmName(heatResult.getBmName().trim()).cpuSku(heatResult.getCpuSku().trim()).nodes(heatResult.getNodes()).runType(heatResult.getRunType()).build();
        heatMapRepo.save(heatMap);
    }


    public HeatMap getSingleHeatResult(String bm_name, String cpu_sku, int nodes, String runType) {

        HeatMap list = null;
        list = heatMapRepo.getHeatResult(bm_name,cpu_sku,nodes,runType);

        if(list ==null){
            return null;
        }
        return list;
    }


    public List<AverageResult> getAvgResultCPUApp(String cpu_sku,String app_name) {

        List<AverageResult> list = null;
        list = averageResultRepo.getAverageResultCPUApp(cpu_sku,app_name);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;
    }


    public List<String> getApp() {

        List<String> app_list = null;
        app_list = averageResultRepo.getAPP();

        if(app_list ==null){
            return Collections.EMPTY_LIST;
        }
        return app_list;
    }


    public List<HeatMap> getHeatMapData(String cpu, String type) {
        List<HeatMap> list = null;
        list = heatMapRepo.findHeatMapData(cpu,type);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;

    }

    public List<HeatMap> getHeatMapData(String cpu, String type, String category) {
        List<HeatMap> list = null;
        list = heatMapRepo.findHeatMapData(cpu,type, category);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;

    }



}
