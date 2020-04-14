package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.repo.AverageResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

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
        if (averageResult== null || averageResult.getCpu_sku() == "" || averageResult.getCpu_sku().equals(null) || averageResult.getBm_name() == "" || averageResult.getBm_name().equals(null) )
            return;
        AverageResult avg = AverageResult.builder().app_name(averageResult.getApp_name()).avg_result(averageResult.getAvg_result()).bm_name(averageResult.getBm_name()).cores(averageResult.getCores()).cpu_sku(averageResult.getCpu_sku()).nodes(averageResult.getNodes()).build();
        averageResultRepo.save(averageResult);
    }

    public List<AverageResult> getAverageResult() {
        List<AverageResult> list = null;
        list = averageResultRepo.findAll();
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
}
