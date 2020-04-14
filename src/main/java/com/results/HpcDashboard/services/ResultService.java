package com.results.HpcDashboard.services;

import com.results.HpcDashboard.controller.AverageResultController;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ResultService {
    @Autowired
    ResultRepo resultRepo;

    @Autowired
    Util util;

    @Autowired
    AverageResultService averageResultService;


    @Transactional
    public void insertResult(String[] resultData){
        String bm_name = resultData[2];
        String cpu = resultData[7].toUpperCase();
        int nodes = Integer.valueOf(resultData[3]);
        String app_name = resultData[1];
        int cores = Integer.valueOf(resultData[4]);

        Result result = Result.builder().job_id(resultData[0]).app_name(app_name).bm_name(bm_name).nodes(nodes).cores(cores).node_name(resultData[5].replaceAll("\\\\,",",")).result(util.round(Double.valueOf(resultData[6]),4)).cpu(cpu).build();
        resultRepo.save(result);

        List<Double> list = getResultsForAverage(bm_name,cpu,nodes);
        double avgResult = util.calculateAverageResult(list);

        AverageResult averageResult = averageResultService.getSingleAvgResult(bm_name,cpu,nodes);

        if(averageResult == null){
         AverageResult aResult = AverageResult.builder().app_name(app_name).bm_name(bm_name).cores(cores).cpu_sku(cpu).avg_result(avgResult).nodes(nodes).build();
         averageResultService.insertAverageResult(aResult);
        }
        else{
            averageResultService.updateAverageResult(cpu,nodes,bm_name,avgResult);
        }

    }
    public List<Double> getResultsForAverage(String bm_name, String cpu, int nodes){
        return resultRepo.findresultsByAppCPUNode(bm_name,cpu,nodes);
    }


    public List<Result> getAllResults(){
        return resultRepo.findAll();
    }
}
