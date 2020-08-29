package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.heatMap.*;
import com.results.HpcDashboard.dto.partComparision.CompareResult;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.HeatMap;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.services.HeatMapService;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/avg")
public class AverageResultRestController {

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    Util util;

    @Autowired
    HeatMapService heatMapService;

    @Autowired
    ChartRestController chartRestController;


    public String getLowerHigher(String app){
        HashMap<String,String> appMap = util.getAppMap();
        return appMap.getOrDefault(app,app);
    }


    @GetMapping("/result")
    public List<AverageResult> getAvgResult(){
        List<AverageResult> list = null;
        list = averageResultService.getAverageResult();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }


    @GetMapping("/result/{cpu}/{app_name}/{runType}")
    public List<AverageResult> getAvgResultCPU(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("runType") String runType){
        List<AverageResult> list = null;
          list = averageResultService.getAvgResultCPUAppType(cpu,app_name,runType);
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }


    @GetMapping("/resultComparision/{app_name}/{cpu1}/{cpu2}/{type1}/{type2}")
    public CompareResult getAvgBySelectedCPU(@PathVariable("app_name") String app_name, @PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2, @PathVariable("type1") String type1, @PathVariable("type2") String type2) {

        CompareResult compareResult = null;
        String comment = null;
        List<AverageResult> list1 = averageResultService.getCompDataBySelectedCPU(app_name, cpu1, type1);
        List<AverageResult> list2 = averageResultService.getCompDataBySelectedCPU(app_name, cpu2 ,type2);

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0)
            return compareResult;


        Set<String> bms = new LinkedHashSet<>();
        bms.add("");

        if (list2.size() > list1.size()){
            for (AverageResult avg : list1) {
                bms.add(avg.getBmName());
            }
        }
        else{
            for (AverageResult avg : list2) {
                bms.add(avg.getBmName());
            }
        }

        Map<String,Double> result1 = new LinkedHashMap<>();
        Map<String,Double> result2 = new LinkedHashMap<>();

        Map<String,String> perfDifference = new LinkedHashMap<>();

        for(AverageResult a : list1){
            result1.put(a.getBmName(),a.getAvgResult());
        }
        for(AverageResult a : list2){
            result2.put(a.getBmName(),a.getAvgResult());
        }

        perfDifference.put("","Perf diff(%)");

        Set<String> keys = null;

        if(result2.size() > result1.size())
         keys= result1.keySet();
        else
         keys= result2.keySet();


        for(String k : keys){

            double val1 = result1.getOrDefault(k,0.0);
            double val2 = result2.getOrDefault(k,0.0);

            if(val1 != 0.0 && val2 != 0.0 ) {

                if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                    if (Double.compare(val1, val2) < 0) {
                        double d = (val2 - val1) / Math.abs(val1);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "+" + percentage + "%");
                    } else if (Double.compare(val1, val2) > 0) {
                        double d = Math.abs(val2 - val1) / Math.abs(val1);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "-" + percentage + "%");
                    } else {
                        perfDifference.put(k, 0 + "%");
                    }
                    comment = "Higher is better";
                } else {
                    if (Double.compare(val1, val2) > 0) {
                        double d = (val1 - val2) / Math.abs(val2);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "+" + percentage + "%");
                    } else if (Double.compare(val1, val2) < 0) {
                        double d = Math.abs(val1 - val2) / Math.abs(val2);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "-" + percentage + "%");
                    } else {
                        perfDifference.put(k, 0 + "%");
                    }
                    comment = "Lower is better";
                }
            }
        }

        Map<String,String> res1 = new LinkedHashMap<>();
        Map<String,String> res2 = new LinkedHashMap<>();

        res1.put("",cpu1+"_"+type1);
        res2.put("",cpu2+"_"+type2);

        for(String k : keys){
            res1.put(k,result1.getOrDefault(k,0.0).toString());
            res2.put(k,result2.getOrDefault(k,0.0).toString());
        }

        List<Map<String,String>> dataSets = new ArrayList<>();
        dataSets.add(res1);
        dataSets.add(res2);
        dataSets.add(perfDifference);

         compareResult = CompareResult.builder().appName(app_name).bmName(bms).resultData(dataSets).comment(comment).build();

        return compareResult;

    }


    @GetMapping("/heatMap/{cpu1}/{cpu2}/{type1}/{type2}")
    public HeatMapOutput getHeatMapData(@PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2, @PathVariable("type1") String type1, @PathVariable("type2") String type2) {
        HeatMapOutput heatMapOutput = new HeatMapOutput();
        List<Category> categories = new ArrayList<>();
        List<HeatMapResult> resList = new ArrayList<>();
        List<HeatMap> list1 = heatMapService.getHeatMapData(cpu1,type1);
        List<HeatMap> list2 = heatMapService.getHeatMapData(cpu2,type2);


        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 )
            return heatMapOutput;


        if(list1.size() < list2.size()) {
            Set<String> bms = list1.stream()
                    .map(HeatMap::getBmName)
                    .collect(Collectors.toSet());
             list2 = list2.stream()
                    .filter(result -> bms.contains(result.getBmName()))
                    .collect(Collectors.toList());

            Set<String> bms1 = list2.stream()
                    .map(HeatMap::getBmName)
                    .collect(Collectors.toSet());
            list1 = list1.stream()
                    .filter(result -> bms1.contains(result.getBmName()))
                    .collect(Collectors.toList());
        }
        else if(list1.size() > list2.size()){
            Set<String> bms = list2.stream()
                    .map(HeatMap::getBmName)
                    .collect(Collectors.toSet());
            list1 = list1.stream()
                    .filter(result -> bms.contains(result.getBmName()))
                    .collect(Collectors.toList());

            Set<String> bms1 = list1.stream()
                    .map(HeatMap::getBmName)
                    .collect(Collectors.toSet());
            list2 = list2.stream()
                    .filter(result -> bms1.contains(result.getBmName()))
                    .collect(Collectors.toList());

   }

        LinkedHashSet<String> category = list1.stream()
                .map(HeatMap::getCategory)
                .collect(Collectors.toCollection( LinkedHashSet::new));

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 )
            return heatMapOutput;

        Map<String, Double> bmResList1 = new LinkedHashMap<>();
        Map<String, Double> bmResList2 = new LinkedHashMap<>();

        for(HeatMap h : list1){

            bmResList1.put(h.getBmName(),h.getAvgResult());
        }


        for(HeatMap h : list2){

            bmResList2.put(h.getBmName(),h.getAvgResult());
        }

        for (String cat:category ) {

            Category category1 = new Category();
            category1.setCategory(cat);

            double catAvg =0;
            double appAvg=0;

            List<HeatMap> list3 = heatMapService.getHeatMapData(cpu1,type1,cat);
            List<HeatMap> list4 = heatMapService.getHeatMapData(cpu2,type2,cat);


            if(list3.size() < list4.size()) {
                LinkedHashSet<String> isv = list4.stream()
                        .map(HeatMap::getIsv)
                        .collect(Collectors.toCollection( LinkedHashSet::new));
                list3 = list3.stream()
                        .filter(result -> isv.contains(result.getIsv()))
                        .collect(Collectors.toList());

                LinkedHashSet<String> isv1 = list4.stream()
                        .map(HeatMap::getIsv)
                        .collect(Collectors.toCollection( LinkedHashSet::new));
                list3 = list3.stream()
                        .filter(result -> isv1.contains(result.getIsv()))
                        .collect(Collectors.toList());
            }
            else if(list3.size() > list4.size()){
                LinkedHashSet<String> isv = list4.stream()
                        .map(HeatMap::getIsv)
                        .collect(Collectors.toCollection( LinkedHashSet::new));
                list3 = list3.stream()
                        .filter(result -> isv.contains(result.getIsv()))
                        .collect(Collectors.toList());

                LinkedHashSet<String> isv1 = list3.stream()
                        .map(HeatMap::getIsv)
                        .collect(Collectors.toCollection( LinkedHashSet::new));
                list4 = list4.stream()
                        .filter(result -> isv1.contains(result.getIsv()))
                        .collect(Collectors.toList());

            }

            if(list3.size() < list4.size()) {
                LinkedHashSet<String> bms = list3.stream()
                        .map(HeatMap::getBmName)
                        .collect(Collectors.toCollection( LinkedHashSet::new));
                list4 = list4.stream()
                        .filter(result -> bms.contains(result.getBmName()))
                        .collect(Collectors.toList());

                LinkedHashSet<String> bms1 = list4.stream()
                        .map(HeatMap::getBmName)
                        .collect(Collectors.toCollection( LinkedHashSet::new));
                list3 = list3.stream()
                        .filter(result -> bms1.contains(result.getBmName()))
                        .collect(Collectors.toList());
            }
            else if(list3.size() > list4.size()){
                Set<String> bms = list4.stream()
                        .map(HeatMap::getBmName)
                        .collect(Collectors.toSet());
                list3 = list3.stream()
                        .filter(result -> bms.contains(result.getBmName()))
                        .collect(Collectors.toList());

                Set<String> bms1 = list3.stream()
                        .map(HeatMap::getBmName)
                        .collect(Collectors.toSet());
                list4 = list4.stream()
                        .filter(result -> bms1.contains(result.getBmName()))
                        .collect(Collectors.toList());

            }


            Set<String> isv = new LinkedHashSet<>();
            for (HeatMap heatMap : list3) {
                if (cat.contains(heatMap.getCategory())) {
                    isv.add(heatMap.getIsv());
                }
            }

            if(isv.size() < 1)
            {
                continue;
            }

            Set<ISV> isvList = new LinkedHashSet<>();

            for(String i: isv)
            {
                ISV isv1 = new ISV();

                isv1.setIsv(i);


                Set<String> appName = new LinkedHashSet<>();
                for (HeatMap heatMap : list3) {
                    if (i.contains(heatMap.getIsv())) {
                        appName.add(heatMap.getAppName());
                    }
                }

                if(appName.size() < 1)
                {
                    continue;
                }

                double aAvg =0;
                int aCount =0;

                Set<App> appList = new LinkedHashSet<>();

                for(String a:appName)
                {
                    App app = new App();
                    app.setApplication(chartRestController.getAppName(a));

                    Set<String> bmName = new LinkedHashSet<>();
                    for (HeatMap heatMap : list3) {
                        if (a.contains(heatMap.getAppName())) {
                            bmName.add(heatMap.getBmName());
                        }
                    }

                    if(bmName.size() < 1)
                    {
                        continue;
                    }


                    Map<String, Double> bmUplift = new LinkedHashMap<>();
                    double bAvg =0;
                    int bCount =0;
                    for (String b : bmName ) {

                        double val1 = bmResList1.getOrDefault(b,0.0);
                        double val2 = bmResList2.getOrDefault(b,0.0);
                        double d = 0;
                        double percentage = 0;
                        int flag = 0;
                        if(val1 != 0.0 && val2 != 0.0 ) {

                            if (getLowerHigher(a.trim().toLowerCase()).equals("HIGHER")) {
                                if (Double.compare(val1, val2) < 0) {
                                    flag =0;
                                     d = (val2 - val1) / Math.abs(val1); //+
                                     percentage = util.round(d * 100, 2);

                                } else if (Double.compare(val1, val2) > 0) {
                                    flag =1;
                                     d = Math.abs(val2 - val1) / Math.abs(val1);
                                     percentage = util.round(d * 100, 2);

                                } else {
                                    percentage =0;
                                }

                            } else {
                                if (Double.compare(val1, val2) > 0) {
                                    flag =0;
                                     d = (val1 - val2) / Math.abs(val2);
                                     percentage = util.round(d * 100, 2); //+
                                }
                                else if (Double.compare(val1, val2) < 0) {
                                    flag =1;
                                    d = Math.abs(val1 - val2) / Math.abs(val2);
                                     percentage = util.round(d * 100, 2);  //-
                                }
                                else {
                                    percentage =0;
                                }

                            }
                        }

                        if(flag==1)
                        {
                            percentage = -1.0 * percentage;
                        }

                        bAvg += percentage;
                        bCount++;
                        bmUplift.put(b,percentage);

                    }
                    app.setBmUplift(bmUplift);

                     appAvg =  util.round(bAvg/bCount, 2);

                    app.setUplift(appAvg);
                    aAvg += appAvg;
                    aCount++;

                appList.add(app);
                }

                catAvg =  util.round(aAvg/aCount, 2);
                isv1.setApp(appList);
                isvList.add(isv1);
            }
            category1.setIsvList(isvList);
            category1.setUplift(catAvg);
            categories.add(category1);
            }

        HeatMapResult h;

        for(Category c : categories)
        {
            h = new HeatMapResult();

            h.setCategory(c.getCategory());


            if(c.getUplift() > 0.0) {
                h.setUplift("+"+String.valueOf(c.getUplift())+"%");
            }
            else{
                 h.setUplift(String.valueOf(c.getUplift())+"%");
            }
            resList.add(h);

            for(ISV i: c.getIsvList()){

                for(App a : i.getApp()) {
                      h = new HeatMapResult();
                      h.setISV(i.getIsv());
                      h.setApplication(a.getApplication());

                    if(a.getUplift() > 0.0) {
                        h.setUplift("+"+String.valueOf(a.getUplift())+"%");
                    }
                    else{
                        h.setUplift(String.valueOf(a.getUplift())+"%");
                    }

                      resList.add(h);
                      for(Map.Entry<String, Double> b : a.getBmUplift().entrySet())
                      {
                          h = new HeatMapResult();
                          h.setBenchmark(b.getKey());

                          if(b.getValue() > 0.0) {
                              h.setUplift("+"+String.valueOf(b.getValue())+"%");
                          }
                          else{

                              h.setUplift(String.valueOf(b.getValue())+"%");
                          }
                          resList.add(h);
                      }

                }

            }

        }

        heatMapOutput.setHeatMapResults(resList);

        List<String> columns = new ArrayList<>();

        columns.add("category");
        columns.add("isv");
        columns.add("application");
        columns.add("benchmark");
        columns.add("uplift");

        heatMapOutput.setColumns(columns);

        return heatMapOutput;
    }



    }
