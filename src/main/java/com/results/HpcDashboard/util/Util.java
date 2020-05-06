package com.results.HpcDashboard.util;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.dto.CPUDto;
import com.results.HpcDashboard.models.CPU;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class Util {

    private final String regex = "(?<!\\\\)" + Pattern.quote(",");
    private HashMap<String,String> appMap = new HashMap<>();
    private HashMap<String,String> metricMap = new HashMap<>();

    public List<BenchmarkDto> getBenchmarks(EntityManager entityManager) {
        String queryStr = "select b.app_name,a.bm_name,a.bm_full_name,a.bm_dur,a.bm_metric,a.bm_size, a.bm_size_units,a.bm_dur_units,a.bm_units,a.est_runtime from benchmarks a JOIN applications b on a.app_name=b.app_id";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            List<Object[]> objectList = query.getResultList();
            List<BenchmarkDto> result = new ArrayList<>();
            for (Object[] row : objectList) {
                result.add(new BenchmarkDto(row));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public  List<CPUDto> findAllCPUs(EntityManager entityManager) {
        String queryStr = "select DISTINCT a.cpu_sku, b.cores from average_result a, cpu_info b where a.cpu_sku=b.cpu_sku;";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            List<Object[]> objectList = query.getResultList();
            List<CPUDto> list = new ArrayList<>();
            for (Object[] row : objectList) {
                CPUDto c = new CPUDto(row);
                list.add(new CPUDto(c.getCpuSku(),c.getCores()));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public String[] performRegex(String individualResult){
        String[] resultData=null;
        individualResult = individualResult.substring(0,individualResult.indexOf("["))+individualResult.substring(individualResult.indexOf("["),individualResult.indexOf("]")).replaceAll(",","\\\\,")+individualResult.substring(individualResult.indexOf("]"));
        resultData = individualResult.split(regex);
        return resultData;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double calculateAverageResult(List<Double> resultList){

        double total = 0.0;
        int len = resultList.size();
        for(double d : resultList){
            total+=d;
        }
        double average = round(total/len,2);
        return average;
    }

        public HashMap<String,String> getAppMap() {
            appMap.put("abaqus", "LOWER");
            appMap.put("acusolve", "LOWER");
            appMap.put("cfx","LOWER");
            appMap.put("fluent","HIGHER");
            appMap.put("gromacs","HIGHER");
            appMap.put("hpcg","LOWER");
            appMap.put("hpl","HIGHER");
            appMap.put("hycom","HIGHER");
            appMap.put("lammps","HIGHER");
            appMap.put("liggghts","LOWER");
            appMap.put("lsdyna", "LOWER");
            appMap.put("namd", "HIGHER");
            appMap.put("openfoam", "LOWER");
            appMap.put("pamcrash", "LOWER");
            appMap.put("quantum-espresso", "HIGHER");
            appMap.put("radioss", "LOWER");
            appMap.put("starccm", "LOWER");
            appMap.put("stream", "HIGHER");
            appMap.put("wrf", "LOWER");
            appMap.put("cp2k","LOWER");

            return appMap;
        }

    public HashMap<String,String> getMetricMap() {
        metricMap.put("abaqus", "Elapsed Time");
        metricMap.put("acusolve", "Elapsed Time");
        metricMap.put("cfx","Core Solver Rating");
        metricMap.put("fluent","Core Solver Rating");
        metricMap.put("gromacs","ns/day");
        metricMap.put("hpcg","");
        metricMap.put("hpl","TFLOPS");
        metricMap.put("hycom","");
        metricMap.put("lammps","Number of Atoms");
        metricMap.put("liggghts","Elapsed Time");
        metricMap.put("lsdyna", "Elapsed Time");
        metricMap.put("namd", "ns/day");
        metricMap.put("openfoam", "Elapsed Time");
        metricMap.put("pamcrash", "Elapsed Time");
        metricMap.put("quantum-espresso", "");
        metricMap.put("radioss", "Elapsed Time");
        metricMap.put("starccm", "Elapsed Time");
        metricMap.put("stream", "Bandwidth (MB/s)");
        metricMap.put("wrf", "Mean Timer/Step");
        metricMap.put("cp2k","");

        return metricMap;
    }



}
