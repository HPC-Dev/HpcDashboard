package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.JobDto;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.AverageResultRepo;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
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

    @PersistenceContext
    private EntityManager entityManager;

    public String getCpuGen(String cpu) {
        HashMap<String, String> cpuGenMap = util.getCpuGenMap();
        return cpuGenMap.getOrDefault(cpu, "");
    }

    @Transactional
    public int deleteJobs(String[] jobIds) {
        int count=0;
        for(String job : jobIds){
            if(resultRepo.existsById(job)) {
                JobDto j = util.findJobDetails(entityManager, job);
                resultRepo.deleteById(job);
                List<Double> list = getResultsForAverage(j.getBmName(), j.getCpu(), j.getNodes());

                if (list.size() > 0) {
                    double avgResult = util.calculateAverageResult(list);
                    double coefficientOfVariation = util.resultCoefficientOfVariation(list);
                    int runCount = list.size();
                    averageResultService.updateAverageResult(j.getCpu(), j.getNodes(), j.getBmName(), avgResult, coefficientOfVariation, runCount);
                } else {
                    averageResultService.deleteAverageResult(j.getCpu(), j.getNodes(), j.getBmName());
                }
            }
            else{
                count++;
            }
        }
        return count;

    }

    @Transactional
    public void insertResult(String[] resultData){
        String bm_name = resultData[2].trim().toLowerCase();
        String cpu = resultData[7].trim();
        int nodes = Integer.valueOf(resultData[3]);
        String app_name = resultData[1].trim().toLowerCase();
        int cores = Integer.valueOf(resultData[4]);

        Result result = Result.builder().jobId(resultData[0]).appName(app_name).bmName(bm_name).nodes(nodes).cores(cores).nodeName(resultData[5].replaceAll("\\\\,",",")).result(util.round(Double.valueOf(resultData[6]),4)).cpu(cpu).build();
        resultRepo.save(result);

        List<Double> list = getResultsForAverage(bm_name,cpu,nodes);
        double avgResult = util.calculateAverageResult(list);
        double coefficientOfVariation = util.resultCoefficientOfVariation(list);
        int runCount = list.size();
        AverageResult averageResult = averageResultService.getSingleAvgResult(bm_name,cpu,nodes);

        if(averageResult == null){
            //insert CV
         AverageResult aResult = AverageResult.builder().appName(app_name).bmName(bm_name).cores(cores).cpuSku(cpu).avgResult(avgResult).nodes(nodes).coefficientOfVariation(coefficientOfVariation).runCount(runCount).build();
         averageResultService.insertAverageResult(aResult);
        }
        else{
            averageResultService.updateAverageResult(cpu,nodes,bm_name,avgResult,coefficientOfVariation,runCount);
        }

    }

    @Transactional
    public void insertResultCsv(List<Result> results){
        for(Result result: results) {
            if(result.getCpuGen().equals("") || result.getCpuGen().equals(null) || result.getCpuGen().equals(" ")){
                result.setCpuGen(getCpuGen(result.getCpu().trim()));
            }
            resultRepo.save(result);
            List<Double> list = getResultsForAverage(result.getBmName().trim().toLowerCase(),result.getCpu().trim().toLowerCase(),result.getNodes());
            double avgResult = util.calculateAverageResult(list);
            double coefficientOfVariation = util.resultCoefficientOfVariation(list);
            int runCount = list.size();
            AverageResult averageResult = averageResultService.getSingleAvgResult(result.getBmName().trim().toLowerCase(),result.getCpu(),result.getNodes());
            if(averageResult == null){
                    //insert CV
                AverageResult aResult = AverageResult.builder().appName(result.getAppName().trim().toLowerCase()).bmName(result.getBmName().trim().toLowerCase()).cores(result.getCores()).cpuSku(result.getCpu()).avgResult(avgResult).nodes(result.getNodes()).coefficientOfVariation(coefficientOfVariation).runCount(runCount).build();
                averageResultService.insertAverageResult(aResult);
            }
            else{
                    averageResultService.updateAverageResult(result.getCpu(), result.getNodes(), result.getBmName().trim().toLowerCase(), avgResult,coefficientOfVariation,runCount);
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

    public List<String> getCpu(String cpuGen) {

        List<String> cpu_list = null;
        cpu_list = resultRepo.getCPUGen(cpuGen);

        if(cpu_list ==null){
            return Collections.EMPTY_LIST;
        }
        return cpu_list;
    }

    public List<Integer> getNodes() {

        List<Integer> node_list = null;
        node_list = resultRepo.getNodes();

        if(node_list ==null){
            return Collections.EMPTY_LIST;
        }
        return node_list;
    }

    public List<String> getOS() {

        List<String> os = null;
        os = resultRepo.getOS();

        if(os ==null){
            return Collections.EMPTY_LIST;
        }
        return os;
    }

    public List<String> getCluster() {

        List<String> cluster = null;
        cluster = resultRepo.getCluster();

        if(cluster ==null){
            return Collections.EMPTY_LIST;
        }
        return cluster;
    }

    public List<String> getUser() {

        List<String> users = null;
        users = resultRepo.getUsers();

        if(users ==null){
            return Collections.EMPTY_LIST;
        }
        return users;
    }

    public List<String> getPlatform() {

        List<String> platform = null;
        platform = resultRepo.getPlatform();

        if(platform ==null){
            return Collections.EMPTY_LIST;
        }
        return platform;
    }

    public List<String> getCpuGen() {

        List<String> cpuGen = null;
        cpuGen = resultRepo.getCpuGen();

        if(cpuGen ==null){
            return Collections.EMPTY_LIST;
        }
        return cpuGen;
    }

    public List<String> getRunType() {

        List<String> run_type = null;
        run_type = resultRepo.getRunType();

        if(run_type ==null){
            return Collections.EMPTY_LIST;
        }
        return run_type;
    }


}
