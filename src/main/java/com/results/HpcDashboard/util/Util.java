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

    public Map<String,Integer> findAllCPUs(EntityManager entityManager) {
        String queryStr = "select cpu_sku,cores from cpu_info";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            List<Object[]> objectList = query.getResultList();
            Map<String,Integer> map = new HashMap<>();
            for (Object[] row : objectList) {
                CPUDto c = new CPUDto(row);
                map.put(c.getCpu_sku(),c.getCores());
            }
            return map;
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
        double average = round(total/len,4);
        return average;
    }


}
