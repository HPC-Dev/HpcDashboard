package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.JobDto;
import com.results.HpcDashboard.models.AppCategory;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.HeatMap;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.AppCategoryRepo;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    AppCategoryService appCategoryService;

    @Autowired
    HeatMapService heatMapService;

    @PersistenceContext
    private EntityManager entityManager;

    public String getCpuGen(String cpu) {
        HashMap<String, String> cpuGenMap = util.getCpuGenMap();
        String cpuVal = cpuGenMap.getOrDefault(cpu, "");
        if(cpuVal.equals("") || cpuVal.equals(null))
        {
          if(cpu.toLowerCase().startsWith("milan")){
              cpuVal = "Milan";
          }
          else if(cpu.toLowerCase().startsWith("rome")){
              cpuVal  ="Rome";
          }
          else{
              cpuVal="";
          }
        }
        return cpuVal;
    }

    @Transactional
    public int deleteJobs(String[] jobIds) {
        int count=0;
        for(String job : jobIds){
            if(resultRepo.existsById(job)) {
                JobDto j = util.findJobDetails(entityManager, job);
                resultRepo.deleteById(job);
                List<Double> list = getResultsForAverage(j.getBmName(), j.getCpu(), j.getNodes(), j.getRunType());

                if (list.size() > 0) {
                    double avgResult = util.calculateAverageResult(list);
                    double perCorePerf = util.round(avgResult/j.getCores(),4);
                    double coefficientOfVariation = util.resultCoefficientOfVariation(list);
                    int runCount = list.size();
                    averageResultService.updateAverageResult(j.getCpu(), j.getNodes(), j.getBmName(), avgResult,perCorePerf, coefficientOfVariation, runCount, j.getRunType());
                    heatMapService.updateHeatResult(j.getCpu(), j.getNodes(), j.getBmName().trim().toLowerCase(), avgResult,perCorePerf,runCount, j.getRunType());

                } else {
                    averageResultService.deleteAverageResult(j.getCpu(), j.getNodes(), j.getBmName(), j.getRunType());
                    heatMapService.deleteHeatResult(j.getCpu(), j.getNodes(), j.getBmName(), j.getRunType());
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

        List<Double> list = getResultsForAverage(bm_name,cpu,nodes,"baseline");
        double avgResult = util.calculateAverageResult(list);
        double perCorePerf = util.round(avgResult/cores,4);
        double coefficientOfVariation = util.resultCoefficientOfVariation(list);
        int runCount = list.size();
        AverageResult averageResult = averageResultService.getSingleAvgResult(bm_name,cpu,nodes,"baseline");

        if(averageResult == null){
            //insert CV
         AverageResult aResult = AverageResult.builder().appName(app_name).bmName(bm_name).cores(cores).cpuSku(cpu).avgResult(avgResult).perCorePerf(perCorePerf).nodes(nodes).coefficientOfVariation(coefficientOfVariation).runCount(runCount).runType("baseline").build();
         averageResultService.insertAverageResult(aResult);
        }
        else{
            averageResultService.updateAverageResult(cpu,nodes,bm_name,avgResult,perCorePerf,coefficientOfVariation,runCount,"baseline");
        }

    }

    @Transactional
    public void insertResultCsv(List<Result> results) throws ParseException {
        for(Result result: results) {

            if(result.getCpu() != null)
            result.setCpuGen(getCpuGen(result.getCpu().trim()));

            if(result.getTime() != null)
                result.setTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(result.getTime().trim()));

            if(!(result.getRunType().equals("baseline")) &&  result.getRunType() != null && result.getSetting() !=null)
            {
                result.setRunType(result.getRunType()+"_"+result.getSetting());
            }
            else if(result.getRunType() != null){
                result.setRunType(result.getRunType());
            }
            else{
                result.setRunType("baseline");

            }

            result.setResult(util.round(result.getResult(),4));

            resultRepo.save(result);

            AppCategory appCategory = appCategoryService.getSingleCategory(result.getBmName());

            List<Double> list;
            if(result.getRunType().equals("baseline"))
            {
                 list = getResultsForAverage(result.getBmName().trim().toLowerCase(),result.getCpu().trim().toLowerCase(),result.getNodes(),"baseline");
            }
            else
            {
                 list = getResultsForAverage(result.getBmName().trim().toLowerCase(),result.getCpu().trim().toLowerCase(),result.getNodes(), result.getRunType());
            }

            double avgResult = util.calculateAverageResult(list);
            double coefficientOfVariation = util.resultCoefficientOfVariation(list);
            int runCount = list.size();
            AverageResult averageResult;
            HeatMap heatMapResult;

            double perCorePerf = util.round(avgResult/result.getCores(),4);

            if(result.getRunType().equals("baseline")) {
                averageResult = averageResultService.getSingleAvgResult(result.getBmName().trim().toLowerCase(), result.getCpu(), result.getNodes(),"baseline");
                heatMapResult = heatMapService.getSingleHeatResult(result.getBmName().trim().toLowerCase(), result.getCpu(), result.getNodes(),"baseline");
            }
            else{
                averageResult = averageResultService.getSingleAvgResult(result.getBmName().trim().toLowerCase(), result.getCpu(), result.getNodes(), result.getRunType());
                heatMapResult = heatMapService.getSingleHeatResult(result.getBmName().trim().toLowerCase(), result.getCpu(), result.getNodes(),result.getRunType());
            }


            if(averageResult == null){

                AverageResult aResult;
                if(result.getRunType().equals("baseline")) {
                    aResult = AverageResult.builder().appName(result.getAppName().trim().toLowerCase()).bmName(result.getBmName().trim().toLowerCase()).cores(result.getCores()).cpuSku(result.getCpu()).avgResult(avgResult).perCorePerf(perCorePerf).nodes(result.getNodes()).coefficientOfVariation(coefficientOfVariation).runCount(runCount).runType("baseline").build();
                }
                else {
                    aResult = AverageResult.builder().appName(result.getAppName().trim().toLowerCase()).bmName(result.getBmName().trim().toLowerCase()).cores(result.getCores()).cpuSku(result.getCpu()).avgResult(avgResult).perCorePerf(perCorePerf).nodes(result.getNodes()).coefficientOfVariation(coefficientOfVariation).runCount(runCount).runType(result.getRunType()).build();
                }
                    averageResultService.insertAverageResult(aResult);
                }

            else{
                if(result.getRunType().equals("baseline")) {
                    averageResultService.updateAverageResult(result.getCpu(), result.getNodes(), result.getBmName().trim().toLowerCase(), avgResult,perCorePerf, coefficientOfVariation, runCount, "baseline");
                }
                else{
                    averageResultService.updateAverageResult(result.getCpu(), result.getNodes(), result.getBmName().trim().toLowerCase(), avgResult,perCorePerf, coefficientOfVariation, runCount, result.getRunType());
                }
            }


            if(heatMapResult == null){

                HeatMap heatResult;
                if(result.getRunType().equals("baseline")) {
                    heatResult = HeatMap.builder().category(appCategory.getCategory()).isv(appCategory.getIsv()).appName(result.getAppName().trim().toLowerCase()).bmName(result.getBmName().trim().toLowerCase()).cores(result.getCores()).cpuSku(result.getCpu()).perCorePerf(perCorePerf).avgResult(avgResult).nodes(result.getNodes()).runCount(runCount).runType("baseline").build();
                }
                else {
                    heatResult = HeatMap.builder().category(appCategory.getCategory()).isv(appCategory.getIsv()).appName(result.getAppName().trim().toLowerCase()).bmName(result.getBmName().trim().toLowerCase()).cores(result.getCores()).cpuSku(result.getCpu()).perCorePerf(perCorePerf).avgResult(avgResult).nodes(result.getNodes()).runCount(runCount).runType(result.getRunType()).build();
                }
                heatMapService.insertHeatResult(heatResult);
            }

            else{
                if(result.getRunType().equals("baseline")) {
                    heatMapService.updateHeatResult(result.getCpu(), result.getNodes(), result.getBmName().trim().toLowerCase(), avgResult,perCorePerf, runCount, "baseline");
                }
                else{
                    heatMapService.updateHeatResult(result.getCpu(), result.getNodes(), result.getBmName().trim().toLowerCase(), avgResult,perCorePerf, runCount,  result.getRunType());
                }
            }
        }
    }

    public List<Double> getResultsForAverage(String bm_name, String cpu, int nodes, String runType){
        return resultRepo.findresultsByAppCPUNode(bm_name,cpu,nodes,runType);
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


    public List<String> getBIOS() {

        List<String> bios = null;
        bios = resultRepo.getBIOS();

        if(bios ==null){
            return Collections.EMPTY_LIST;
        }
        return bios;
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
