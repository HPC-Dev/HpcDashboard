package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.repo.AverageResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AverageResultService {

    @Autowired
    AverageResultRepo averageResultRepo;


    @Transactional
    public void updateAverageResult(String cpu_sku, int nodes, String bm_name, double avg) {
        if(cpu_sku == "" || cpu_sku.equals(null) || bm_name == "" || bm_name.equals(null))
            return;
        averageResultRepo.updateAverageResult(bm_name,cpu_sku,nodes,avg);
    }

    @Transactional
    public void deleteAverageResult(String cpu_sku, int nodes, String bm_name) {
        if(cpu_sku == "" || cpu_sku.equals(null) || bm_name == "" || bm_name.equals(null) )
            return;
        averageResultRepo.deleteAverageResult(bm_name,cpu_sku,nodes);
    }

    @Transactional
    public void insertAverageResult(AverageResult averageResult) {
        if (averageResult== null || averageResult.getCpuSku() == "" || averageResult.getCpuSku().equals(null) || averageResult.getBmName() == "" || averageResult.getBmName().equals(null) )
            return;
        //insert variance below
        AverageResult avg = AverageResult.builder().appName(averageResult.getAppName()).avgResult(averageResult.getAvgResult()).bmName(averageResult.getBmName()).cores(averageResult.getCores()).cpuSku(averageResult.getCpuSku()).nodes(averageResult.getNodes()).build();
        averageResultRepo.save(averageResult);
    }

    public List<AverageResult> getAverageResult() {
        Iterable<AverageResult> results = averageResultRepo.findAll();
        List<AverageResult> list = null;
        list = StreamSupport
                .stream(results.spliterator(), false)
                .collect(Collectors.toList());
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }

    public AverageResult getSingleAvgResult(String bm_name, String cpu_sku, int nodes) {

        AverageResult list = null;
        list = averageResultRepo.getAverageResult(bm_name,cpu_sku,nodes);

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

    public List<AverageResult> getAvgResultCPUAppBm(String cpu_sku,String app_name, String bm_name) {

        List<AverageResult> list = null;
        list = averageResultRepo.getAverageResultCPUAppBm(cpu_sku,app_name,bm_name);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    public List<AverageResult> getAvgResultCPUAppNode(String cpu_sku,String app_name, int node) {

        List<AverageResult> list = null;
        list = averageResultRepo.getAverageResultCPUAppNode(cpu_sku,app_name,node);

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

    public List<String> getCpu() {

        List<String> cpu_list = null;
        cpu_list = averageResultRepo.getCPU();

        if(cpu_list ==null){
            return Collections.EMPTY_LIST;
        }
        return cpu_list;
    }

    public List<AverageResult> getBySelectedCPU(String app_name,List<String> cpus) {

        List<AverageResult> list = null;
        list = averageResultRepo.findBySelectedCPU(app_name,cpus);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;
    }
}
