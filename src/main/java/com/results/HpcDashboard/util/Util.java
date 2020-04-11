package com.results.HpcDashboard.util;

import com.results.HpcDashboard.dto.BenchmarkDto;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
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

    public String[] performRegex(String individualResult){
        String[] resultData=null;
        individualResult = individualResult.substring(0,individualResult.indexOf("["))+individualResult.substring(individualResult.indexOf("["),individualResult.indexOf("]")).replaceAll(",","\\\\,")+individualResult.substring(individualResult.indexOf("]"));
        resultData = individualResult.split(regex);
        return resultData;
    }

}
