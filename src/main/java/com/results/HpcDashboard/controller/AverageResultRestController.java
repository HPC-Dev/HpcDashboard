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

import java.lang.reflect.Array;
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

    public static List<HeatMap> filterList(List<HeatMap> list,Set<String> bmsList )
    {
        for (int i = 0; i < list.size(); i++)
        {
            if(bmsList.contains(list.get(i).getBmName()))
            {
                continue;
            }
            else{
                list.remove(i);
                i--;
            }

        }
        return list;

    }

    public static Set<String> filterBmList(List<HeatMap> list1,List<HeatMap> list2)
    {
        Set<String> bmsList = new LinkedHashSet<>();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).getBmName().equals(list2.get(j).getBmName())) {
                    bmsList.add(list1.get(i).getBmName());
                }
            }
        }
        return bmsList;
    }

    public static List<List<HeatMap>> filterLists(List<HeatMap> list1,List<HeatMap> list2,List<HeatMap> list3,List<HeatMap> list4)
    {
        List<List<HeatMap>> filteredResult = new ArrayList<>();
        Set<String> bmsList1 = filterBmList(list1,list2);
        Set<String> bmsList2 = filterBmList(list1,list3);
        Set<String> bmsList3 = filterBmList(list1,list4);

        bmsList2.retainAll(bmsList1);
        bmsList3.retainAll(bmsList2);
        bmsList1.retainAll(bmsList3);

        Set<String> bmsList = bmsList1;


        list1 = filterList(list1,bmsList);
        list2 = filterList(list2,bmsList);
        list3 = filterList(list3,bmsList);
        list4 = filterList(list4,bmsList);

        filteredResult.add(list1);
        filteredResult.add(list2);
        filteredResult.add(list3);
        filteredResult.add(list4);

        return filteredResult;
    }

    public static List<List<HeatMap>> filterLists(List<HeatMap> list1,List<HeatMap> list2)
    {
        List<List<HeatMap>> filteredResult = new ArrayList<>();
        Set<String> bmsList = filterBmList(list1,list2);


        list1 = filterList(list1,bmsList);
        list2 = filterList(list2,bmsList);



        filteredResult.add(list1);
        filteredResult.add(list2);

        return filteredResult;
    }


    private void getHetMapNodeResult( String cpu1, String type1, String cpu2, String type2, String cpu3, String type3, String cpu4, String type4, LinkedHashSet<String> category, Map<String, Double> bmResList1, Map<String, Double> bmResList2, Map<String, PerCore> perCoreListFirst, Map<String, PerCore> perCoreListSecond, List<Category> categories) {

        List<List<HeatMap>> filteredLists = null;
        for (String cat:category ) {

            Category category1 = new Category();
            category1.setCategory(cat);

            double catAvg =0;
            double appAvg=0;

            double perCoreCatAvg =0;
            double perCoreAppAvg=0;

            List<HeatMap> list5 = heatMapService.getHeatMapData(cpu1,type1,cat);
            List<HeatMap> list6 = heatMapService.getHeatMapData(cpu2,type2,cat);
            List<HeatMap> list7 = heatMapService.getHeatMapData(cpu3,type3,cat);
            List<HeatMap> list8 = heatMapService.getHeatMapData(cpu4,type4,cat);


            filteredLists = filterLists(list5,list6,list7,list8);


            list5 = filteredLists.get(0);
            list6 = filteredLists.get(1);

            Set<String> isv = new LinkedHashSet<>();
            for (HeatMap heatMap : list5) {
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
                for (HeatMap heatMap : list5) {
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
                    for (HeatMap heatMap : list5) {
                        if (a.contains(heatMap.getAppName())) {
                            bmName.add(heatMap.getBmName());
                        }
                    }

                    if(bmName.size() < 1)
                    {
                        continue;
                    }


                    double bAvg =0;
                    int bCount =0;
                    double perCoreBAvg =0;
                    int perCoreBCount =0;

                    Map<String, Double> bmUplift = new LinkedHashMap<>();
                    Map<String, Double> perCoreBmUplift = new LinkedHashMap<>();

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

                        PerCore v1 = perCoreListFirst.getOrDefault(b,new PerCore());

                        PerCore v2 = perCoreListSecond.getOrDefault(b,new PerCore());
                        double d = 0;
                        double percentage = 0;
                        int flag = 0;
                        double d1 =0;
                        double d2 =0;
                        int core1 = Integer.valueOf(v1.getCores());
                        int core2 = Integer.valueOf(v2.getCores());

                        double val1 = v1.getResult();
                        double val2 = v2.getResult();


                        if(v1.getResult() != 0.0 && v2.getResult() != 0.0 ) {

                            if (getLowerHigher(a.trim().toLowerCase()).equals("HIGHER")) {

                                d1 = val2/val1;
                                d2 = (double)core1/core2;
                                d = d1 * d2;
                                d = d -1;

                            } else {

                                d1 = val1/val2;
                                d2 = (double)core1/core2;
                                d = d1 * d2;
                                d = d - 1;
                            }

                            percentage =  (util.round(d * 100, 2));
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

    }

    private List<HeatMapResult> getConsolidatedResult(List<Category> categories, List<HeatMapResult> resList) {

        HeatMapResult h;
        HashMap<String,Double> perCorePercentage = new HashMap<>();;

        for(Category c : categories)
        {
            h = new HeatMapResult();

            h.setCategory(c.getCategory());


            if(c.getUplift() > 0.0) {
                h.setPerNode1("+"+String.valueOf(c.getUplift())+"%");
            }
            else{
                h.setPerNode1(String.valueOf(c.getUplift())+"%");
            }

            if(c.getPer_Core_Uplift() > 0.0) {
                h.setPerCore1("+"+String.valueOf(c.getPer_Core_Uplift())+"%");
            }
            else{
                h.setPerCore1(String.valueOf(c.getPer_Core_Uplift())+"%");
            }

            resList.add(h);

            for(ISV i: c.getIsvList()){

                for(App a : i.getApp()) {
                    h = new HeatMapResult();
                    h.setISV(i.getIsv());
                    h.setApplication(a.getApplication());

                    if(a.getUplift() > 0.0) {
                        h.setPerNode1("+"+String.valueOf(a.getUplift())+"%");
                    }
                    else{
                        h.setPerNode1(String.valueOf(a.getUplift())+"%");
                    }


                    if(a.getPer_Core_Uplift() > 0.0) {
                        h.setPerCore1("+"+String.valueOf(a.getPer_Core_Uplift())+"%");
                    }
                    else{
                        h.setPerCore1(String.valueOf(a.getPer_Core_Uplift())+"%");
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
                            h.setPerNode1("+"+String.valueOf(b.getValue())+"%");
                        }
                        else{

                            h.setPerNode1(String.valueOf(b.getValue())+"%");
                        }

                        if(perCoreUplift > 0.0) {
                            h.setPerCore1("+"+String.valueOf(perCoreUplift)+"%");
                        }
                        else{
                            h.setPerCore1(String.valueOf(perCoreUplift)+"%");
                        }


                        resList.add(h);
                    }
                }

            }

        }
        return  resList;
    }

    @GetMapping("/heatMap/{cpu1}/{cpu2}/{cpu3}/{cpu4}/{type1}/{type2}/{type3}/{type4}")
    public HeatMapOutput getHeatMapData(@PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2,@PathVariable("cpu3") String cpu3, @PathVariable("cpu4") String cpu4,  @PathVariable("type1") String type1, @PathVariable("type2") String type2 ,  @PathVariable("type3") String type3, @PathVariable("type4") String type4) {
        HeatMapOutput heatMapOutput = new HeatMapOutput();
        List<Category> categories = new ArrayList<>();
        List<HeatMapResult> resList = new ArrayList<>();
        List<HeatMap> list1 = heatMapService.getHeatMapData(cpu1,type1);
        List<HeatMap> list2 = heatMapService.getHeatMapData(cpu2,type2);

        List<HeatMap> list3 = heatMapService.getHeatMapData(cpu3,type3);
        List<HeatMap> list4 = heatMapService.getHeatMapData(cpu4,type4);

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 )
            return heatMapOutput;

        Set<String> bmsList = new LinkedHashSet<>();

        List<List<HeatMap>> filteredLists = null;

        filteredLists = filterLists(list1,list2,list3,list4);

        list1 = filteredLists.get(0);
        list2 = filteredLists.get(1);
        list3 = filteredLists.get(2);
        list4 = filteredLists.get(3);


        LinkedHashSet<String> category = list1.stream()
                .map(HeatMap::getCategory)
                .collect(Collectors.toCollection( LinkedHashSet::new));

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 )
            return heatMapOutput;

        Map<String, Double> bmResList1 = new LinkedHashMap<>();
        Map<String, Double> bmResList2 = new LinkedHashMap<>();
        Map<String, Double> bmResList3 = new LinkedHashMap<>();
        Map<String, Double> bmResList4 = new LinkedHashMap<>();


        Map<String, PerCore> perCoreListFirst = new LinkedHashMap<>();

        Map<String, PerCore> perCoreListSecond = new LinkedHashMap<>();

        Map<String, PerCore> perCoreListThird = new LinkedHashMap<>();

        Map<String, PerCore> perCoreListFourth = new LinkedHashMap<>();


        for(HeatMap h : list1){

            bmResList1.put(h.getBmName(),h.getAvgResult());
            perCoreListFirst.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "baseline"));

        }


        for(HeatMap h : list2){

            bmResList2.put(h.getBmName(),h.getAvgResult());
            perCoreListSecond.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "compartitive"));
        }

        for(HeatMap h : list3){

            bmResList3.put(h.getBmName(),h.getAvgResult());
            perCoreListThird.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "compartitive"));


        }

        for(HeatMap h : list4){

            bmResList4.put(h.getBmName(),h.getAvgResult());
            perCoreListFourth.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "compartitive"));
        }


        List<Category> categories1 = new ArrayList<>();
        List<Category> categories2 = new ArrayList<>();
        List<HeatMapResult> resList1 = new ArrayList<>();
        List<HeatMapResult> resList2 = new ArrayList<>();


        getHetMapNodeResult(cpu1, type1, cpu2, type2, cpu3, type3, cpu4, type4,  category,  bmResList1, bmResList2, perCoreListFirst, perCoreListSecond, categories);
        getHetMapNodeResult(cpu1, type1, cpu3, type3, cpu2, type2, cpu4, type4, category,  bmResList1, bmResList3, perCoreListFirst, perCoreListThird, categories1);
        getHetMapNodeResult(cpu1, type1, cpu4, type4, cpu2, type2, cpu3, type3, category,  bmResList1, bmResList4, perCoreListFirst, perCoreListFourth, categories2);

        getConsolidatedResult(categories, resList);
        getConsolidatedResult(categories1, resList1);
        getConsolidatedResult(categories2, resList2);


        for(int i=0,j=0,k=0; i<resList.size() && j<resList1.size() && k<resList2.size(); ++i,++j,++k) {

            HeatMapResult h = resList.get(i);
            HeatMapResult h1 = resList1.get(i);
            HeatMapResult h2 = resList2.get(i);

            h.setPerCore2(h1.getPerCore1());
            h.setPerCore3(h2.getPerCore1());
            h.setPerNode2(h1.getPerNode1());
            h.setPerNode3(h2.getPerNode1());
        }

        heatMapOutput.setHeatMapResults(resList);

        List<String> columns = new ArrayList<>();

        columns.add("category");
        columns.add("isv");
        columns.add("application");
        columns.add("benchmark");
        columns.add("perNode1");
        columns.add("perNode2");
        columns.add("perNode3");
        columns.add("perCore1");
        columns.add("perCore2");
        columns.add("perCore3");

        heatMapOutput.setColumns(columns);

        return heatMapOutput;
    }


//    @GetMapping("/heatMap/{cpu1}/{cpu2}/{cpu3}/{cpu4}/{type1}/{type2}/{type3}/{type4}")
//    public HeatMapOutput getHeatMapDataOld(@PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2,@PathVariable("cpu3") String cpu3, @PathVariable("cpu4") String cpu4,  @PathVariable("type1") String type1, @PathVariable("type2") String type2 ,  @PathVariable("type3") String type3, @PathVariable("type4") String type4) {
//        HeatMapOutput heatMapOutput = new HeatMapOutput();
//        List<Category> categories = new ArrayList<>();
//        List<HeatMapResult> resList = new ArrayList<>();
//        List<HeatMap> list1 = heatMapService.getHeatMapData(cpu1,type1);
//        List<HeatMap> list2 = heatMapService.getHeatMapData(cpu2,type2);
//
//        List<HeatMap> list3_new = heatMapService.getHeatMapData(cpu3,type3);
//        List<HeatMap> list4_new = heatMapService.getHeatMapData(cpu4,type4);
//
//        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 )
//            return heatMapOutput;
//
//        Set<String> bmsList = new LinkedHashSet<>();
//
//        List<List<HeatMap>> filteredLists = null;
//        filteredLists = filterLists(list1,list2);
//
//        list1 = filteredLists.get(0);
//        list2 = filteredLists.get(1);
//
//
//        LinkedHashSet<String> category = list1.stream()
//                .map(HeatMap::getCategory)
//                .collect(Collectors.toCollection( LinkedHashSet::new));
//
//        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 )
//            return heatMapOutput;
//
//        Map<String, Double> bmResList1 = new LinkedHashMap<>();
//        Map<String, Double> bmResList2 = new LinkedHashMap<>();
//        Map<String, Double> bmResList3 = new LinkedHashMap<>();
//        Map<String, Double> bmResList4 = new LinkedHashMap<>();
//
//
//        Map<String, PerCore> perCoreListFirst = new LinkedHashMap<>();
//
//        Map<String, PerCore> perCoreListSecond = new LinkedHashMap<>();
//
//
//        Map<String, PerCore> perCoreListThird = new LinkedHashMap<>();
//
//        Map<String, PerCore> perCoreListFourth = new LinkedHashMap<>();
//
//
//        for(HeatMap h : list1){
//
//            bmResList1.put(h.getBmName(),h.getAvgResult());
//            perCoreListFirst.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "baseline"));
//
//        }
//
//
//        for(HeatMap h : list2){
//
//            bmResList2.put(h.getBmName(),h.getAvgResult());
//            perCoreListSecond.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "compartitive"));
//        }
//
//        for(HeatMap h : list3_new){
//
//            bmResList3.put(h.getBmName(),h.getAvgResult());
//            perCoreListThird.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "compartitive"));
//
//
//        }
//
//        for(HeatMap h : list4_new){
//
//            bmResList4.put(h.getBmName(),h.getAvgResult());
//            perCoreListFourth.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "compartitive"));
//
//
//        }
//
//        for (String cat:category ) {
//
//            Category category1 = new Category();
//            category1.setCategory(cat);
//
//            double catAvg =0;
//            double appAvg=0;
//
//            double perCoreCatAvg =0;
//            double perCoreAppAvg=0;
//
//            List<HeatMap> list3 = heatMapService.getHeatMapData(cpu1,type1,cat);
//            List<HeatMap> list4 = heatMapService.getHeatMapData(cpu2,type2,cat);
//
//            filteredLists = filterLists(list3,list4);
//
//            list3 = filteredLists.get(0);
//            list4 = filteredLists.get(1);
//
//            Set<String> isv = new LinkedHashSet<>();
//            for (HeatMap heatMap : list3) {
//                if (cat.contains(heatMap.getCategory())) {
//                    isv.add(heatMap.getIsv());
//                }
//            }
//
//            if(isv.size() < 1)
//            {
//                continue;
//            }
//
//            Set<ISV> isvList = new LinkedHashSet<>();
//
//            double aAvg =0;
//            int aCount =0;
//
//            double perCoreAAvg =0;
//            int perCoreACount =0;
//
//
//            for(String i: isv)
//            {
//                ISV isv1 = new ISV();
//
//                isv1.setIsv(i);
//
//
//                Set<String> appName = new LinkedHashSet<>();
//                for (HeatMap heatMap : list3) {
//                    if (i.contains(heatMap.getIsv())) {
//                        appName.add(heatMap.getAppName());
//                    }
//                }
//
//                if(appName.size() < 1)
//                {
//                    continue;
//                }
//
//                Set<App> appList = new LinkedHashSet<>();
//
//                for(String a:appName)
//                {
//                    App app = new App();
//                    app.setApplication(chartRestController.getAppName(a));
//
//                    Set<String> bmName = new LinkedHashSet<>();
//                    for (HeatMap heatMap : list3) {
//                        if (a.contains(heatMap.getAppName())) {
//                            bmName.add(heatMap.getBmName());
//                        }
//                    }
//
//                    if(bmName.size() < 1)
//                    {
//                        continue;
//                    }
//
//
//                    double bAvg =0;
//                    int bCount =0;
//                    double perCoreBAvg =0;
//                    int perCoreBCount =0;
//
//                    Map<String, Double> bmUplift = new LinkedHashMap<>();
//                    Map<String, Double> perCoreBmUplift = new LinkedHashMap<>();
//
//                    for (String b : bmName ) {
//
//                        double val1 = bmResList1.getOrDefault(b,0.0);
//                        double val2 = bmResList2.getOrDefault(b,0.0);
//                        double d = 0;
//                        double percentage = 0;
//                        int flag = 0;
//                        if(val1 != 0.0 && val2 != 0.0 ) {
//
//                            if (getLowerHigher(a.trim().toLowerCase()).equals("HIGHER")) {
//                                if (Double.compare(val1, val2) < 0) {
//                                    flag =0;
//                                    d = (val2 - val1) / Math.abs(val1); //+
//                                    percentage = util.round(d * 100, 2);
//
//                                } else if (Double.compare(val1, val2) > 0) {
//                                    flag =1;
//                                    d = Math.abs(val2 - val1) / Math.abs(val1);
//                                    percentage = util.round(d * 100, 2);
//
//                                } else {
//                                    percentage =0;
//                                }
//
//                            } else {
//                                if (Double.compare(val1, val2) > 0) {
//                                    flag =0;
//                                    d = (val1 - val2) / Math.abs(val2);
//                                    percentage = util.round(d * 100, 2); //+
//                                }
//                                else if (Double.compare(val1, val2) < 0) {
//                                    flag =1;
//                                    d = Math.abs(val1 - val2) / Math.abs(val2);
//                                    percentage = util.round(d * 100, 2);  //-
//                                }
//                                else {
//                                    percentage =0;
//                                }
//
//                            }
//                        }
//
//                        if(flag==1)
//                        {
//                            percentage = -1.0 * percentage;
//                        }
//
//                        bAvg += percentage;
//                        bCount++;
//                        bmUplift.put(b,percentage);
//
//                    }
//
//                    for (String b : bmName ) {
//
//                        PerCore v1 = perCoreListFirst.getOrDefault(b,new PerCore());
//
//                        PerCore v2 = perCoreListSecond.getOrDefault(b,new PerCore());
//                        double d = 0;
//                        double percentage = 0;
//                        int flag = 0;
//                        double d1 =0;
//                        double d2 =0;
//                        int core1 = Integer.valueOf(v1.getCores());
//                        int core2 = Integer.valueOf(v2.getCores());
//
//                        double val1 = v1.getResult();
//                        double val2 = v2.getResult();
//
//
//                        if(v1.getResult() != 0.0 && v2.getResult() != 0.0 ) {
//
//                            if (getLowerHigher(a.trim().toLowerCase()).equals("HIGHER")) {
//
//                                d1 = val2/val1;
//                                d2 = (double)core1/core2;
//                                d = d1 * d2;
//                                d = d -1;
//
//                            } else {
//
//                                d1 = val1/val2;
//                                d2 = (double)core1/core2;
//                                d = d1 * d2;
//                                d = d - 1;
//                            }
//
//                            percentage =  (util.round(d * 100, 2));
//                        }
//
//                        if(flag==1)
//                        {
//                            percentage = -1.0 * percentage;
//                        }
//
//                        perCoreBAvg += percentage;
//                        perCoreBCount++;
//                        perCoreBmUplift.put(b,percentage);
//
//                    }
//
//                    app.setBmUplift(bmUplift);
//                    app.setPerCoreBmUplift(perCoreBmUplift);
//
//
//                    appAvg =  util.round(bAvg/bCount, 2);
//
//                    perCoreAppAvg =  util.round(perCoreBAvg/perCoreBCount, 2);
//
//                    app.setUplift(appAvg);
//                    app.setPer_Core_Uplift(perCoreAppAvg);
//
//                    aAvg += appAvg;
//                    aCount++;
//
//                    perCoreAAvg += perCoreAppAvg;
//                    perCoreACount++;
//
//                    appList.add(app);
//                }
//
//
//                catAvg =  util.round(aAvg/aCount, 2);
//                perCoreCatAvg = util.round(perCoreAAvg/perCoreACount, 2);
//
//                isv1.setApp(appList);
//                isvList.add(isv1);
//            }
//            category1.setIsvList(isvList);
//            category1.setUplift(catAvg);
//            category1.setPer_Core_Uplift(perCoreCatAvg);
//            categories.add(category1);
//        }
//
//        HeatMapResult h;
//        HashMap<String,Double> perCorePercentage = new HashMap<>();;
//
//        for(Category c : categories)
//        {
//            h = new HeatMapResult();
//
//            h.setCategory(c.getCategory());
//
//
//            if(c.getUplift() > 0.0) {
//                h.setPerNode("+"+String.valueOf(c.getUplift())+"%");
//            }
//            else{
//                h.setPerNode(String.valueOf(c.getUplift())+"%");
//            }
//
//            if(c.getPer_Core_Uplift() > 0.0) {
//                h.setPerCore("+"+String.valueOf(c.getPer_Core_Uplift())+"%");
//            }
//            else{
//                h.setPerCore(String.valueOf(c.getPer_Core_Uplift())+"%");
//            }
//
//            resList.add(h);
//
//            for(ISV i: c.getIsvList()){
//
//                for(App a : i.getApp()) {
//                    h = new HeatMapResult();
//                    h.setISV(i.getIsv());
//                    h.setApplication(a.getApplication());
//
//                    if(a.getUplift() > 0.0) {
//                        h.setPerNode("+"+String.valueOf(a.getUplift())+"%");
//                    }
//                    else{
//                        h.setPerNode(String.valueOf(a.getUplift())+"%");
//                    }
//
//
//                    if(a.getPer_Core_Uplift() > 0.0) {
//                        h.setPerCore("+"+String.valueOf(a.getPer_Core_Uplift())+"%");
//                    }
//                    else{
//                        h.setPerCore(String.valueOf(a.getPer_Core_Uplift())+"%");
//                    }
//
//
//                    resList.add(h);
//
//
//                    for(Map.Entry<String, Double> b : a.getPerCoreBmUplift().entrySet())
//                    {
//                        perCorePercentage.put(b.getKey(), b.getValue());
//                    }
//
//
//
//                    for(Map.Entry<String, Double> b : a.getBmUplift().entrySet())
//                    {
//                        h = new HeatMapResult();
//                        h.setBenchmark(b.getKey());
//
//                        double perCoreUplift = perCorePercentage.get(b.getKey());
//
//                        if(b.getValue() > 0.0) {
//                            h.setPerNode("+"+String.valueOf(b.getValue())+"%");
//                        }
//                        else{
//
//                            h.setPerNode(String.valueOf(b.getValue())+"%");
//                        }
//
//                        if(perCoreUplift > 0.0) {
//                            h.setPerCore("+"+String.valueOf(perCoreUplift)+"%");
//                        }
//                        else{
//                            h.setPerCore(String.valueOf(perCoreUplift)+"%");
//                        }
//
//
//                        resList.add(h);
//                    }
//                }
//
//            }
//
//        }
//
//        heatMapOutput.setHeatMapResults(resList);
//
//        List<String> columns = new ArrayList<>();
//
//        columns.add("category");
//        columns.add("isv");
//        columns.add("application");
//        columns.add("benchmark");
//        columns.add("perNode");
//        columns.add("perCore");
//
//        heatMapOutput.setColumns(columns);
//
//        return heatMapOutput;
//    }
//


}
