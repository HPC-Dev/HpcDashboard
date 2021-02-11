package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.heatMap.*;
import com.results.HpcDashboard.dto.partComparison.*;
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


    @GetMapping("/resultComparison/{app_name}/{cpu1}/{cpu2}/{type1}/{type2}")
    public CompareResult getAvgBySelectedCPU(@PathVariable("app_name") String app_name, @PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2, @PathVariable("type1") String type1, @PathVariable("type2") String type2) {

        CompareResult compareResult = null;
        String comment = null;
        List<AverageResult> list1 = averageResultService.getCompDataBySelectedCPU(app_name, cpu1, type1);
        List<AverageResult> list2 = averageResultService.getCompDataBySelectedCPU(app_name, cpu2 ,type2);

        Set<String> bms = new LinkedHashSet<>();
        bms.add("");
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).getBmName().equals(list2.get(j).getBmName())) {
                    bms.add(list1.get(i).getBmName());
                }
            }
        }

        if(bms.size() <2 )
            return compareResult;



        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0)
            return compareResult;


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

    public static List<List<HeatMap>> filterLists(List<HeatMap> list1,List<HeatMap> list2)
    {
        List<List<HeatMap>> filteredResult = new ArrayList<>();
        Set<String> bmsList = new LinkedHashSet<>();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).getBmName().equals(list2.get(j).getBmName())) {
                    bmsList.add(list1.get(i).getBmName());
                }
            }
        }

        for (int i = 0; i < list1.size(); i++)
        {
            if(bmsList.contains(list1.get(i).getBmName()))
            {
                continue;
            }
            else{
                list1.remove(i);
                i--;
            }

        }

        for (int i = 0; i < list2.size(); i++)
        {
            if(bmsList.contains(list2.get(i).getBmName()))
            {
                continue;
            }
            else{
                list2.remove(i);
                i--;
            }

        }

        filteredResult.add(list1);
        filteredResult.add(list2);

    return filteredResult;
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

        Set<String> bmsList = new LinkedHashSet<>();

        List<List<HeatMap>> filteredLists = null;
        filteredLists = filterLists(list1,list2);

        list1 = filteredLists.get(0);
        list2 = filteredLists.get(1);


        LinkedHashSet<String> category = list1.stream()
                .map(HeatMap::getCategory)
                .collect(Collectors.toCollection( LinkedHashSet::new));

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 )
            return heatMapOutput;

        Map<String, Double> bmResList1 = new LinkedHashMap<>();
        Map<String, Double> bmResList2 = new LinkedHashMap<>();

        Map<String, Double> perCoreList1 = new LinkedHashMap<>();
        Map<String, Double> perCoreList2 = new LinkedHashMap<>();

        for(HeatMap h : list1){

            bmResList1.put(h.getBmName(),h.getAvgResult());
            perCoreList1.put(h.getBmName(),h.getPerCorePerf());
        }


        for(HeatMap h : list2){

            bmResList2.put(h.getBmName(),h.getAvgResult());
            perCoreList2.put(h.getBmName(),h.getPerCorePerf());
        }

        for (String cat:category ) {

            Category category1 = new Category();
            category1.setCategory(cat);

            double catAvg =0;
            double appAvg=0;

            double perCoreCatAvg =0;
            double perCoreAppAvg=0;

            List<HeatMap> list3 = heatMapService.getHeatMapData(cpu1,type1,cat);
            List<HeatMap> list4 = heatMapService.getHeatMapData(cpu2,type2,cat);

            filteredLists = filterLists(list3,list4);

            list3 = filteredLists.get(0);
            list4 = filteredLists.get(1);

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

            double aAvg =0;
            int aCount =0;

            double perCoreAAvg =0;
            int perCoreACount =0;


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
                    Map<String, Double> perCoreBmUplift = new LinkedHashMap<>();

                    double bAvg =0;
                    int bCount =0;
                    double perCoreBAvg =0;
                    int perCoreBCount =0;

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

                    for (String b : bmName ) {

                        double val1 = perCoreList1.getOrDefault(b,0.0);
                        double val2 = perCoreList2.getOrDefault(b,0.0);
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

                        perCoreBAvg += percentage;
                        perCoreBCount++;
                        perCoreBmUplift.put(b,percentage);

                    }

                    app.setBmUplift(bmUplift);
                    app.setPerCoreBmUplift(perCoreBmUplift);


                     appAvg =  util.round(bAvg/bCount, 2);

                    perCoreAppAvg =  util.round(perCoreBAvg/perCoreBCount, 2);

                    app.setUplift(appAvg);
                    app.setPer_Core_Uplift(perCoreAppAvg);

                    aAvg += appAvg;
                    aCount++;

                    perCoreAAvg += perCoreAppAvg;
                    perCoreACount++;

                appList.add(app);
                }


                catAvg =  util.round(aAvg/aCount, 2);
                perCoreCatAvg = util.round(perCoreAAvg/perCoreACount, 2);

                isv1.setApp(appList);
                isvList.add(isv1);
            }
            category1.setIsvList(isvList);
            category1.setUplift(catAvg);
            category1.setPer_Core_Uplift(perCoreCatAvg);
            categories.add(category1);
            }

        HeatMapResult h;
        HashMap<String,Double> perCorePercentage = new HashMap<>();;

        for(Category c : categories)
        {
            h = new HeatMapResult();

            h.setCategory(c.getCategory());


            if(c.getUplift() > 0.0) {
                h.setPerNode("+"+String.valueOf(c.getUplift())+"%");
            }
            else{
                 h.setPerNode(String.valueOf(c.getUplift())+"%");
            }

            if(c.getPer_Core_Uplift() > 0.0) {
                h.setPerCore("+"+String.valueOf(c.getPer_Core_Uplift())+"%");
            }
            else{
                h.setPerCore(String.valueOf(c.getPer_Core_Uplift())+"%");
            }

            resList.add(h);

            for(ISV i: c.getIsvList()){

                for(App a : i.getApp()) {
                      h = new HeatMapResult();
                      h.setISV(i.getIsv());
                      h.setApplication(a.getApplication());

                    if(a.getUplift() > 0.0) {
                        h.setPerNode("+"+String.valueOf(a.getUplift())+"%");
                    }
                    else{
                        h.setPerNode(String.valueOf(a.getUplift())+"%");
                    }


                    if(a.getPer_Core_Uplift() > 0.0) {
                        h.setPerCore("+"+String.valueOf(a.getPer_Core_Uplift())+"%");
                    }
                    else{
                        h.setPerCore(String.valueOf(a.getPer_Core_Uplift())+"%");
                    }


                    resList.add(h);


                    for(Map.Entry<String, Double> b : a.getPerCoreBmUplift().entrySet())
                    {
                        perCorePercentage.put(b.getKey(), b.getValue());
                    }



                      for(Map.Entry<String, Double> b : a.getBmUplift().entrySet())
                      {
                          h = new HeatMapResult();
                          h.setBenchmark(b.getKey());

                          double perCoreUplift = perCorePercentage.get(b.getKey());

                          if(b.getValue() > 0.0) {
                              h.setPerNode("+"+String.valueOf(b.getValue())+"%");
                          }
                          else{

                              h.setPerNode(String.valueOf(b.getValue())+"%");
                          }

                          if(perCoreUplift > 0.0) {
                              h.setPerCore("+"+String.valueOf(perCoreUplift)+"%");
                          }
                          else{
                              h.setPerCore(String.valueOf(perCoreUplift)+"%");
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
        columns.add("perNode");
        columns.add("perCore");

        heatMapOutput.setColumns(columns);

        return heatMapOutput;
    }



    }
