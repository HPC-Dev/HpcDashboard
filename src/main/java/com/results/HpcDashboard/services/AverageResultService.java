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
    public void updateAverageResult(String cpu_sku, int nodes, String bm_name, double avg,double perCorePerf, double cv, int count, String runType) {
        if(cpu_sku == "" || cpu_sku.equals(null) || bm_name == "" || bm_name.equals(null))
            return;
        averageResultRepo.updateAverageResult(bm_name,cpu_sku,nodes,avg,perCorePerf,cv,count, runType);
    }

    @Transactional
    public void deleteAverageResult(String cpu_sku, int nodes, String bm_name, String runType) {
        if(cpu_sku == "" || cpu_sku.equals(null) || bm_name == "" || bm_name.equals(null) )
            return;
        averageResultRepo.deleteAverageResult(bm_name,cpu_sku,nodes, runType);
    }

    @Transactional
    public void insertAverageResult(AverageResult averageResult) {
        if (averageResult== null || averageResult.getCpuSku() == "" || averageResult.getCpuSku().equals(null) || averageResult.getBmName() == "" || averageResult.getBmName().equals(null) )
            return;
        //insert CV below
        AverageResult avg = AverageResult.builder().appName(averageResult.getAppName().trim()).avgResult(averageResult.getAvgResult()).perCorePerf(averageResult.getPerCorePerf()).bmName(averageResult.getBmName().trim()).cores(averageResult.getCores()).cpuSku(averageResult.getCpuSku().trim()).nodes(averageResult.getNodes()).coefficientOfVariation(averageResult.getCoefficientOfVariation()).runCount(averageResult.getRunCount()).runType(averageResult.getRunType()).build();
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

    public AverageResult getSingleAvgResult(String bm_name, String cpu_sku, int nodes, String runType) {

        AverageResult list = null;
        list = averageResultRepo.getAverageResult(bm_name,cpu_sku,nodes,runType);

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

    public List<AverageResult> getAvgResultCPUAppType(String cpu_sku,String app_name, String type) {

        List<AverageResult> list = null;
        list = averageResultRepo.getAverageResultCPUAppType(cpu_sku,app_name, type);

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

    public List<AverageResult> getAvgResultCPUAppNode(String cpu_sku,String app_name, int node, String type) {

        List<AverageResult> list = null;
        list = averageResultRepo.getAverageResultCPUAppNode(cpu_sku,app_name,node, type);

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

    public List<String> getApp(String cpu) {

        List<String> app_list = null;
        app_list = averageResultRepo.getAPP(cpu);

        if(app_list ==null){
            return Collections.EMPTY_LIST;
        }
        return app_list;
    }

    public List<String> getAppByType(String cpu, String type) {

        List<String> app_list = null;
        app_list = averageResultRepo.getAppByType(cpu, type);

        if(app_list ==null){
            return Collections.EMPTY_LIST;
        }
        return app_list;
    }

    public List<String> getSelectBm(String app_name, String cpu) {

        List<String> bm_list = null;
        bm_list = averageResultRepo.getSelectedBm(app_name,cpu);

        if(bm_list ==null){
            return Collections.EMPTY_LIST;
        }
        return bm_list;
    }

    public List<String> getSelectBm(String app_name) {

        List<String> bm_list = null;
        bm_list = averageResultRepo.getSelectedBm(app_name);

        if(bm_list ==null){
            return Collections.EMPTY_LIST;
        }
        return bm_list;
    }

    public List<String> getCpu(String appName) {

        List<String> cpu_list = null;
        cpu_list = averageResultRepo.getCPU(appName);

        if(cpu_list ==null){
            return Collections.EMPTY_LIST;
        }
        return cpu_list;
    }

    public List<String> getJustCpu() {

        List<String> cpu_list = null;
        cpu_list = averageResultRepo.getJustCPU();

        if(cpu_list ==null){
            return Collections.EMPTY_LIST;
        }
        return cpu_list;
    }

    public List<String> getRunTypes(String appName) {

        List<String> runType_list = null;
        runType_list = averageResultRepo.getRunTypes(appName);

        if(runType_list ==null){
            return Collections.EMPTY_LIST;
        }
        return runType_list;
    }

    public List<String> getRunTypesByCPU(String cpu) {

        List<String> runType_list = null;
        runType_list = averageResultRepo.getRunTypesByCPU(cpu);

        if(runType_list ==null){
            return Collections.EMPTY_LIST;
        }
        return runType_list;
    }

    public List<String> getRunTypesByAPPCPU(String appName, String cpu) {

        List<String> runType_list = null;
        runType_list = averageResultRepo.getRunTypesByAPPCPU(appName, cpu);

        if(runType_list ==null){
            return Collections.EMPTY_LIST;
        }
        return runType_list;
    }




    public List<String> getCpuSelected(String appName,  List<String> runTypes) {

        List<String> cpu_list = null;
        cpu_list = averageResultRepo.getCpuSelected(appName,  runTypes);

        if(cpu_list ==null){
            return Collections.EMPTY_LIST;
        }
        return cpu_list;
    }

    public int getNodesCount(String appName, String cpu, String type) {
       int nodeCount = averageResultRepo.getNodesCount(appName, cpu, type);

        return nodeCount;
    }

    public List<String> getCpu() {

        List<String> cpu_list = null;
        cpu_list = averageResultRepo.getCPU();

        if(cpu_list ==null){
            return Collections.EMPTY_LIST;
        }
        return cpu_list;
    }

    public List<AverageResult> getBySelectedCPUApp(String app_name,List<String> cpus,List<String> runTypes) {

        List<AverageResult> list = null;

//        StringBuilder generatedString = new StringBuilder();
//        StringBuilder finalString = new StringBuilder();
//
//        finalString = finalString.append("select * from average_result where app_name= \"openfoam\" and cpu_sku IN (\"7543\",\"74F3\") and run_type IN (\"latest\") and nodes =1 ");
//        for(int i=0; i<cpus.size(); i++)
//        {
//            int value = i+1;
//            StringBuilder s = new StringBuilder();
//            s.append("\"");
//            s.append(cpus.get(i));
//            s.append("\"");
//
//            generatedString = generatedString.append(" when "+ s.toString() +" then " + String.valueOf(value));
//        }
//        finalString.append("ORDER BY case cpu_sku" + generatedString.toString() +  " else 9999 end");

        list = averageResultRepo.findBySelectedCPUApp(app_name,cpus, runTypes);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    public List<AverageResult> getBySelectedCPUAppAsc(String app_name,List<String> cpus, List<String> runTypes) {

        List<AverageResult> list = null;
        list = averageResultRepo.findBySelectedCPUAppAsc(app_name,cpus, runTypes);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    public List<AverageResult> getBySelectedCPUAppDesc(String app_name,List<String> cpus, List<String> runTypes) {

        List<AverageResult> list = null;
        list = averageResultRepo.findBySelectedCPUAppDesc(app_name,cpus, runTypes);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    public List<AverageResult> getCompDataBySelectedCPU(String app_name,String cpu, String type) {

        List<AverageResult> list = null;
        list = averageResultRepo.findCompDataBySelectedCPU(app_name,cpu,type);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    public int getJobExists(String jobId) {

        return averageResultRepo.getJobExists(jobId);
    }

    public List<Integer> getRunCount() {
        List<Integer> run_count = null;
        run_count = averageResultRepo.getRunCount();

        if(run_count ==null){
            return Collections.EMPTY_LIST;
        }
        return run_count;
    }
}
