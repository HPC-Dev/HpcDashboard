package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.ChartsResponse;
import com.results.HpcDashboard.dto.multichart.Dataset;
import com.results.HpcDashboard.dto.multichart.MultiChartResponse;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.services.ApplicationService;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.util.Util;
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

    @Autowired
    Util util;

    static Map<String,String> appMapCamelCase;


    public String getMetric(String app){
        HashMap<String,String> metricMap = util.getMetricMap();
        return metricMap.getOrDefault(app,"");
    }


    public String getLowerHigher(String app){
        HashMap<String,String> appMap = util.getAppMap();
        return appMap.getOrDefault(app,"");
    }


    public static String getAppName(String app){
        appMapCamelCase = new HashMap<>();
        appMapCamelCase.put("abaqus", "Abaqus");
        appMapCamelCase.put("acusolve", "AcuSolve");
        appMapCamelCase.put("cfx","CFX");
        appMapCamelCase.put("fluent","Fluent");
        appMapCamelCase.put("gromacs","GROMACS");
        appMapCamelCase.put("hpcg","HPCG");
        appMapCamelCase.put("hpl","HPL");
        appMapCamelCase.put("hycom","HYCOM");
        appMapCamelCase.put("lammps","LAMMPS");
        appMapCamelCase.put("liggghts","LIGGGHTS");
        appMapCamelCase.put("lsdyna", "LS-DYNA");
        appMapCamelCase.put("namd", "NAMD");
        appMapCamelCase.put("openfoam", "OpenFOAM");
        appMapCamelCase.put("pamcrash", "Pam-Crash");
        appMapCamelCase.put("quantum-espresso", "Quantum ESPRESSO");
        appMapCamelCase.put("radioss", "Radioss");
        appMapCamelCase.put("starccm", "STAR-CCM+");
        appMapCamelCase.put("stream", "STREAM");
        appMapCamelCase.put("wrf", "WRF");
        appMapCamelCase.put("cp2k","CP2K");

        return appMapCamelCase.getOrDefault(app,app);
    }

    @GetMapping("/resultApp/{cpu}/{app_name}/{node}")
    public List<ChartsResponse> getChartResult(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("node") int node){
        List<ChartsResponse> listResponse = new ArrayList<>();
        ChartsResponse chartsResponse = null;
        String comment=null;
        List<AverageResult> list = null;
        list = averageResultService.getAvgResultCPUAppNode(cpu,app_name,node);
        String appCpu = getAppName(app_name)+" - "+cpu;
        String metric = getMetric(app_name.toLowerCase().trim());

        List<String> label = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        if(getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
            comment = "Higher is better";
        }
        else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")){
            comment = "Lower is better";
        }


        for(AverageResult avgRes : list){
            label.add(avgRes.getBmName());
            data.add(avgRes.getAvgResult());
        }
        chartsResponse = ChartsResponse.builder().metric(metric).appCPUName(appCpu).dataset(data).labels(label).comment(comment).build();
        listResponse.add(chartsResponse);
        return listResponse;
    }



    @GetMapping("/resultBm/{cpu}/{app_name}/{bm_name}")
    public List<ChartsResponse> getAvgResultCPUBM(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("bm_name") String bm_name){
        List<ChartsResponse> listResponse = new ArrayList<>();
        ChartsResponse chartsResponse = null;
        List<AverageResult> list = null;
        String comment=null;
        list = averageResultService.getAvgResultCPUAppBm(cpu,app_name,bm_name);
        String appBmCpu = getAppName(app_name)+" - "+bm_name+" - "+cpu;
        String metric = getMetric(app_name.toLowerCase().trim());

        List<String> label = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for(AverageResult avgRes : list){
            label.add(avgRes.getNodes()+" Node");
            data.add(avgRes.getAvgResult());
        }
        if(getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
            comment = "Higher is better";
        }
        else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")){
            comment = "Lower is better";
        }

        chartsResponse = ChartsResponse.builder().metric(metric).appCPUName(appBmCpu).dataset(data).labels(label).comment(comment).build();
        listResponse.add(chartsResponse);
        return listResponse;

    }


    @GetMapping("/result/{app_name}")
    public List<MultiChartResponse> getAvgBySelectedCPUChart(@PathVariable("app_name") String app_name, String[] cpuList ){
        List<String> cpus = Arrays.asList(cpuList);
        String metric = getMetric(app_name.toLowerCase().trim());
        String comment = "";
        List<MultiChartResponse> resultList =new ArrayList<>();
        MultiChartResponse mlr = new MultiChartResponse();
        List<AverageResult> list = null;
        list = averageResultService.getBySelectedCPUApp(app_name,cpus);

        Set<String> cpu = new LinkedHashSet<>();
        Set<String> bms = new LinkedHashSet<>();

        for(AverageResult avg : list){
            cpu.add(avg.getCpuSku());
            bms.add(avg.getBmName());
        }
        if(getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
            comment = "Higher is better";
        }
        else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")){
            comment = "Lower is better";
        }

        List<String> label = new ArrayList<>(bms);
        mlr.setLabels(label);
        mlr.setMetric(metric);
        mlr.setAppName(getAppName(list.get(0).getAppName()));
        mlr.setComment(comment);
        List<Dataset> d = new ArrayList<>();
        for(String c : cpu){

            Dataset data = new Dataset();
            List<Double> res = new ArrayList<>();
            for(AverageResult avg : list)
            {
                if(avg.getCpuSku().equals(c)){

                    res.add(avg.getAvgResult());
                }
            }
            data.setCpuName(c);
            data.setValue(res);
            d.add(data);
        }
        mlr.setDatasets(d);
        resultList.add(mlr);
        if(resultList ==null){
            return Collections.emptyList();
        }
        return resultList;
    }

}
