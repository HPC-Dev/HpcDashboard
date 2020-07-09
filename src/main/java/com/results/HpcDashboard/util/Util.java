package com.results.HpcDashboard.util;


import com.results.HpcDashboard.dto.CPUDto;
import com.results.HpcDashboard.dto.JobDto;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class Util {

    private final String regex = "(?<!\\\\)" + Pattern.quote(",");
    private HashMap<String,String> appMap = new HashMap<>();
    private HashMap<String,String> metricMap = new HashMap<>();
    private HashMap<String,String> cpuGenMap = new HashMap<>();

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


    public JobDto findJobDetails(EntityManager entityManager, String jobId) {
        String queryStr = "select r.bm_name,r.cpu,r.nodes from results r where r.job_id = ?1";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            query.setParameter(1, jobId );
            JobDto job = new JobDto((Object[]) query.getSingleResult());
            return job;
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

    public Date convertTimeStamp(String str) throws ParseException {
        String[] a = str.split("-");
        String[] b = new String[a.length];
        for(int i=0; i<a.length;i++){
            if(a[i].length() < 2){
                b[i] = "0"+a[i];
            }
            else{
                b[i] = a[i];
            }
        }

        String date = b[0]+"-"+b[1]+"-"+b[2]+" "+b[3]+":"+b[4]+":"+b[5];
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse(date);
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
            appMap.put("ofoam", "LOWER");
            appMap.put("pamcrash", "LOWER");
            appMap.put("quantum-espresso", "HIGHER");
            appMap.put("radioss", "LOWER");
            appMap.put("starccm", "LOWER");
            appMap.put("stream", "HIGHER");
            appMap.put("lmp", "HIGHER");
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

    public HashMap<String,String> getCpuGenMap() {

        cpuGenMap.put("7601","Naples");
        cpuGenMap.put("7551P","Naples");
        cpuGenMap.put("7551","Naples");
        cpuGenMap.put("7501","Naples");
        cpuGenMap.put("7451","Naples");
        cpuGenMap.put("7401P","Naples");
        cpuGenMap.put("7401","Naples");
        cpuGenMap.put("7371","Naples");
        cpuGenMap.put("7351P","Naples");
        cpuGenMap.put("7351","Naples");
        cpuGenMap.put("7301","Naples");
        cpuGenMap.put("7281","Naples");
        cpuGenMap.put("7261","Naples");
        cpuGenMap.put("7251","Naples");
        cpuGenMap.put("7F72","Rome");
        cpuGenMap.put("7F52","Rome");
        cpuGenMap.put("7F32","Rome");
        cpuGenMap.put("7H12","Rome");
        cpuGenMap.put("7742","Rome");
        cpuGenMap.put("7702","Rome");
        cpuGenMap.put("7702P","Rome");
        cpuGenMap.put("7662","Rome");
        cpuGenMap.put("7642","Rome");
        cpuGenMap.put("7552","Rome");
        cpuGenMap.put("7542","Rome");
        cpuGenMap.put("7532","Rome");
        cpuGenMap.put("7502","Rome");
        cpuGenMap.put("7502P","Rome");
        cpuGenMap.put("7452","Rome");
        cpuGenMap.put("7402","Rome");
        cpuGenMap.put("7402P","Rome");
        cpuGenMap.put("7352","Rome");
        cpuGenMap.put("7302","Rome");
        cpuGenMap.put("7302P","Rome");
        cpuGenMap.put("7282","Rome");
        cpuGenMap.put("7272","Rome");
        cpuGenMap.put("7262","Rome");
        cpuGenMap.put("7252","Rome");
        cpuGenMap.put("7232P","Rome");
        cpuGenMap.put("6148","Skylake");
        cpuGenMap.put("8180","Skylake");
        cpuGenMap.put("6242R","Cascade");
        cpuGenMap.put("6242","Cascade");
        cpuGenMap.put("6244","Cascade");
        cpuGenMap.put("6248","Cascade");
        cpuGenMap.put("6248R","Cascade");
        cpuGenMap.put("6252","Cascade");
        cpuGenMap.put("8268","Cascade");
        cpuGenMap.put("8280","Cascade");
        cpuGenMap.put("6246R","Cascade");
        cpuGenMap.put("6258R","Cascade");

        return cpuGenMap;
    }

    public double resultCoefficientOfVariation(List<Double> list)
    {
        if(list.size()==1)
            return 0;

        double standardDeviation = 0.0;
        int length = list.size();
        double sum = 0;
        for(double num : list) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: list) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        double sd = Math.sqrt(standardDeviation/length);

        double CV = (sd/mean)*100;

        return round(CV,2);
    }

}
