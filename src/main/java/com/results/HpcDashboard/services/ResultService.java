package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

        Result result = Result.builder().jobId(resultData[0]).appName(app_name).bmName(bm_name).nodes(nodes).cores(cores).nodeName(resultData[5].replaceAll("\\\\,",",")).result(util.round(Double.valueOf(resultData[6]),4)).cpu(cpu).build();
        resultRepo.save(result);

        List<Double> list = getResultsForAverage(bm_name,cpu,nodes);
        double avgResult = util.calculateAverageResult(list);

        AverageResult averageResult = averageResultService.getSingleAvgResult(bm_name,cpu,nodes);

        if(averageResult == null){
            //insert variance
         AverageResult aResult = AverageResult.builder().appName(app_name).bmName(bm_name).cores(cores).cpuSku(cpu).avgResult(avgResult).nodes(nodes).build();
         averageResultService.insertAverageResult(aResult);
        }
        else{
            averageResultService.updateAverageResult(cpu,nodes,bm_name,avgResult);
        }

    }


    @Transactional
    public void insertResultCsv(List<Result> results){
        for(Result result: results) {
            resultRepo.save(result);
            List<Double> list = getResultsForAverage(result.getBmName(),result.getCpu(),result.getNodes());
            double avgResult = util.calculateAverageResult(list);
            AverageResult averageResult = averageResultService.getSingleAvgResult(result.getBmName(),result.getCpu(),result.getNodes());
            if(averageResult == null){
                //insert variance
                AverageResult aResult = AverageResult.builder().appName(result.getAppName()).bmName(result.getBmName()).cores(result.getCores()).cpuSku(result.getCpu()).avgResult(avgResult).nodes(result.getNodes()).build();
                averageResultService.insertAverageResult(aResult);
            }
            else{
                averageResultService.updateAverageResult(result.getCpu(),result.getNodes(),result.getBmName(),avgResult);
            }

        }
    }

    public List<Double> getResultsForAverage(String bm_name, String cpu, int nodes){
        return resultRepo.findresultsByAppCPUNode(bm_name,cpu,nodes);
    }


    public List<Result> getAllResults(){
        Iterable<Result> results = resultRepo.findAll();
        List<Result> list = null;
        list = StreamSupport
                .stream(results.spliterator(), false)
                .collect(Collectors.toList());

        if(list ==null){
            return Collections.emptyList();
        }
    return list;
    }


    public List<String> getApp() {

        List<String> app_list = null;
        app_list = resultRepo.getApp();

        if(app_list ==null){
            return Collections.EMPTY_LIST;
        }
        return app_list;
    }

    public List<String> getCpu() {

        List<String> cpu_list = null;
        cpu_list = resultRepo.getCpu();

        if(cpu_list ==null){
            return Collections.EMPTY_LIST;
        }
        return cpu_list;
    }

    public List<String> getBm() {

        List<String> bm_list = null;
        bm_list = resultRepo.getBm();

        if(bm_list ==null){
            return Collections.EMPTY_LIST;
        }
        return bm_list;
    }

    public List<String> getSelectBm(String app_name) {

        List<String> bm_list = null;
        bm_list = resultRepo.getSelectedBm(app_name);

        if(bm_list ==null){
            return Collections.EMPTY_LIST;
        }
        return bm_list;
    }

    public List<Integer> getNodes() {

        List<Integer> node_list = null;
        node_list = resultRepo.getNodes();

        if(node_list ==null){
            return Collections.EMPTY_LIST;
        }
        return node_list;
    }


}
