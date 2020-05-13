package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.ChartsResponse;
import com.results.HpcDashboard.dto.multichart.*;
import com.results.HpcDashboard.dto.scatter.ScatterChartsResponse;
import com.results.HpcDashboard.dto.scatter.ScatterTableResponse;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.services.ApplicationService;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chart")
public class ChartRestController {

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    Util util;

    static Map<String, String> appMapCamelCase;


    public String getMetric(String app) {
        HashMap<String, String> metricMap = util.getMetricMap();
        return metricMap.getOrDefault(app, "");
    }


    public String getLowerHigher(String app) {
        HashMap<String, String> appMap = util.getAppMap();
        return appMap.getOrDefault(app, "");
    }


    public static String getAppName(String app) {
        appMapCamelCase = new HashMap<>();
        appMapCamelCase.put("abaqus", "Abaqus");
        appMapCamelCase.put("acusolve", "AcuSolve");
        appMapCamelCase.put("cfx", "CFX");
        appMapCamelCase.put("fluent", "Fluent");
        appMapCamelCase.put("gromacs", "GROMACS");
        appMapCamelCase.put("hpcg", "HPCG");
        appMapCamelCase.put("hpl", "HPL");
        appMapCamelCase.put("hycom", "HYCOM");
        appMapCamelCase.put("lammps", "LAMMPS");
        appMapCamelCase.put("liggghts", "LIGGGHTS");
        appMapCamelCase.put("lsdyna", "LS-DYNA");
        appMapCamelCase.put("namd", "NAMD");
        appMapCamelCase.put("openfoam", "OpenFOAM");
        appMapCamelCase.put("pamcrash", "Pam-Crash");
        appMapCamelCase.put("quantum-espresso", "Quantum ESPRESSO");
        appMapCamelCase.put("radioss", "Radioss");
        appMapCamelCase.put("starccm", "STAR-CCM+");
        appMapCamelCase.put("stream", "STREAM");
        appMapCamelCase.put("wrf", "WRF");
        appMapCamelCase.put("cp2k", "CP2K");

        return appMapCamelCase.getOrDefault(app, app);
    }

    @GetMapping("/resultApp/{cpu}/{app_name}/{node}")
    public List<ChartsResponse> getChartResult(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("node") int node) {
        List<ChartsResponse> listResponse = new ArrayList<>();
        ChartsResponse chartsResponse = null;
        String comment = null;
        List<AverageResult> list = null;
        list = averageResultService.getAvgResultCPUAppNode(cpu, app_name, node);

        if (list == null || list.size()==0)
            return listResponse;

        String appCpu = cpu + " - " +  getAppName(app_name) ;
        String metric = getMetric(app_name.toLowerCase().trim());

        List<String> label = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
            comment = "Higher is better";
        } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
            comment = "Lower is better";
        }


        for (AverageResult avgRes : list) {
            label.add(avgRes.getBmName());
            data.add(avgRes.getAvgResult());
        }
        chartsResponse = ChartsResponse.builder().metric(metric).appCPUName(appCpu).dataset(data).labels(label).comment(comment).build();
        listResponse.add(chartsResponse);
        return listResponse;
    }

    @GetMapping("/resultBm/{cpu}/{app_name}")
    public ScatterChartsResponse getAvgResultCPUBM(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name) {
        ScatterChartsResponse scatterChartsResponse = null;
        List<AverageResult> list = null;
        String comment = null;
        list = averageResultService.getAvgResultCPUApp(cpu, app_name);

        if (list == null || list.size()==0)
            return scatterChartsResponse;


        Set<String> cpus = new LinkedHashSet<>();
        Set<String> bms = new LinkedHashSet<>();
        Set<Integer> nodes = new LinkedHashSet<>();

        for (AverageResult avg : list) {
            cpus.add(avg.getCpuSku());
            bms.add(avg.getBmName());
            nodes.add(avg.getNodes());
        }

        List<Map<Integer, Double>> listMap = new ArrayList<>();
        Map<Integer, Double> res = null;

        List<String> bmList = bms.stream().collect(Collectors.toList());

        for (String b : bmList) {
            res = new LinkedHashMap<>();
            for (AverageResult a : list) {
                if (a.getBmName().equals(b)) {

                    res.put(a.getNodes(), a.getAvgResult());
                }
            }
            listMap.add(res);
        }

        String appCpu = cpu + " - " + getAppName(app_name);


        if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
            comment = "Higher is better";
        } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
            comment = "Lower is better";
        }


        List<Map<Integer, Double>> resList = new ArrayList<>();


        Map<Integer, Double> temp = null;

        if(listMap.get(0).size() >2) {
            for (Map<Integer, Double> m : listMap) {
                double firstResult = m.entrySet().iterator().next().getValue();
                temp = new LinkedHashMap<>();
                temp.put(m.entrySet().iterator().next().getKey(), 1.0);
                m.remove(m.entrySet().iterator().next().getKey());

                for (Map.Entry<Integer, Double> entry : m.entrySet()) {
                    double d1;
                    if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
                        d1 = util.round(firstResult / entry.getValue(), 3);
                        temp.put(entry.getKey(), d1);

                    } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                        d1 = util.round(entry.getValue() / firstResult, 3);
                        temp.put(entry.getKey(), d1);
                    }

                }
                resList.add(temp);

            }
        }

        scatterChartsResponse =ScatterChartsResponse.builder().appCPUName(appCpu).comment(comment).labels(bms).dataset(resList).nodes(nodes).build();
        return scatterChartsResponse;
}

    @GetMapping("/multiCPUResult/{app_name}")
    public MultiChartResponse getAvgBySelectedCPUChart(@PathVariable("app_name") String app_name, String[] cpuList) {
        MultiChartResponse multiChartResponse = null;
        List<String> cpus = Arrays.asList(cpuList);
        List<List<Double>> resListFinal = new ArrayList<>();
        List<AverageResult> list = null;
        if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
            list = averageResultService.getBySelectedCPUAppDesc(app_name, cpus);
        }
        else{
            list = averageResultService.getBySelectedCPUAppAsc(app_name, cpus);
        }


        if (list == null || list.size()==0)
            return multiChartResponse;

        Set<String> cpu = new LinkedHashSet<>();
        Set<String> bms = new LinkedHashSet<>();

        for (AverageResult avg : list) {
            cpu.add(avg.getCpuSku());
            bms.add(avg.getBmName());
        }


        List<Map<String, Double>> resList = new ArrayList<>();

        Map<String, Double> res = null;

        List<String> cpulist = cpu.stream().collect(Collectors.toList());
        List<String> bmlist = bms.stream().collect(Collectors.toList());

        for (String bm : bms) {
            res = new LinkedHashMap<>();
            for (AverageResult a : list) {
                if (a.getBmName().equals(bm)) {

                    res.put(a.getCpuSku(), a.getAvgResult());
                }
            }
            resList.add(res);
        }

        List<Map<String, Double>> newResList = new ArrayList<>();

        for(Map<String, Double> e : resList ) {

            Map<String, Double> map = new LinkedHashMap<>();
          for(String c : cpulist) {
              if (!e.containsKey(c)) {
                  map.put(c, 0.0);
              }else{
                  map.put(c,e.get(c));
              }
          }
          newResList.add(map);

        }

        resListFinal = new ArrayList<>();

          List<Double> firstResult = new ArrayList<>();

          List<Double> temp =null;

        String firstCPU = cpulist.get(0);

        for(Map<String, Double> e : newResList){

                firstResult.add(e.get(cpulist.get(0)));

                if(cpulist.get(0).equals(firstCPU))
                e.remove(cpulist.get(0));
          }


          cpulist.remove(0);

        int j=0;
          for(Map<String, Double> e : newResList ){

              if(Double.compare(firstResult.get(j),0.0 ) > 0) {
              temp = new ArrayList<>();
              temp.add(1.0);
              int i=0;
              for(Map.Entry<String,Double> m : e.entrySet()) {

                      if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
                          double d1 = 0.0;

                          if(e.containsKey(cpulist.get(i))) {
                            d1 = util.round(firstResult.get(j) / e.get(cpulist.get(i)), 3);
                          }
                          temp.add(d1);

                      } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                          double d1 = util.round(e.getOrDefault(cpulist.get(i),0.0) / firstResult.get(j), 3);
                          temp.add(d1);
                      }
                      i++;
                  }
              j++;

                  resListFinal.add(temp);
              }
          }

        List<Dataset> dataset = new ArrayList<>();

          for(int i=0; i< resListFinal.size();i++){
              Dataset datasetNew = Dataset.builder().bmName(bmlist.get(i)).value(resListFinal.get(i)).build();
              dataset.add(datasetNew);
          }

          multiChartResponse = MultiChartResponse.builder().appName(getAppName(app_name)).cpus(cpu).dataset(dataset).build();

        return multiChartResponse;
    }

    @GetMapping("/multiCPUTable/{app_name}")
    public MultiChartTableResponse getAvgBySelectedCPUChart2(@PathVariable("app_name") String app_name, String[] cpuList) {

       MultiChartTableResponse multiChartTableResponse = null;
        List<String> cpus = Arrays.asList(cpuList);
        List<Map<String, String>> resListFinal = new ArrayList<>();
        List<AverageResult> list = null;

        if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
            list = averageResultService.getBySelectedCPUAppDesc(app_name, cpus);
        }
        else{

            list = averageResultService.getBySelectedCPUAppAsc(app_name, cpus);
        }


        if (list == null || list.size()==0)
            return multiChartTableResponse;

        Set<String> cpu = new LinkedHashSet<>();
        Set<String> bms = new LinkedHashSet<>();

        for (AverageResult avg : list) {
            cpu.add(avg.getCpuSku());
            bms.add(avg.getBmName());
        }

        List<String> bmlist = bms.stream().collect(Collectors.toList());

        List<Map<String, Double>> resList = new ArrayList<>();

        Map<String, Double> res = null;

        List<String> cpusList = new ArrayList<>();

        for (String c : cpu) {
            res = new LinkedHashMap<>();
            cpusList.add(c);
            for (AverageResult a : list) {
                if (a.getCpuSku().equals(c)) {

                    res.put(a.getBmName(), a.getAvgResult());
                }
            }
            resList.add(res);
        }


        List<Map<String, Double>> newResList = new ArrayList<>();

        for(Map<String, Double> e : resList ) {

            Map<String, Double> map = new LinkedHashMap<>();
            for(String bm : bmlist) {
                if (!e.containsKey(bm)) {
                    map.put(bm, 0.0);
                }else{
                    map.put(bm,e.get(bm));
                }
            }
            newResList.add(map);

        }


        resListFinal = new ArrayList<>();

        Map<String, Double> firstResult = new LinkedHashMap<>();
        firstResult.putAll(newResList.get(0));

        Map<String, String> temp = new LinkedHashMap<>();
        temp.put("", cpusList.get(0));

        for (Map.Entry<String, Double> d : newResList.get(0).entrySet()) {

            if(Double.compare(d.getValue(),0.0 ) > 0)
            temp.put(d.getKey(), "1.0");
        }
        resListFinal.add(temp);

        newResList.remove(0);

        int i=1;

        for (Map<String, Double> lis : newResList) {

            temp = new LinkedHashMap<>();
            temp.put("", cpusList.get(i));
                for (Map.Entry<String, Double> d : lis.entrySet()) {
                    if (Double.compare(firstResult.get(d.getKey()), 0.0) > 0) {
                        if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
                            double d1 = util.round(firstResult.get(d.getKey()) / d.getValue(), 3);
                            temp.put(d.getKey(), String.valueOf(d1));

                        } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                            double d1 = util.round(d.getValue() / firstResult.get(d.getKey()), 3);
                            temp.put(d.getKey(), String.valueOf(d1));
                        }
                    }

                }
                resListFinal.add(temp);
                i++;
            }

        List<String> bmlistFinal = new ArrayList<>();

        for(Map.Entry<String, String> bm : resListFinal.get(0).entrySet())
        {
            bmlistFinal.add(bm.getKey());
        }

        multiChartTableResponse = MultiChartTableResponse.builder().appName(getAppName(app_name)).label(bmlistFinal).resultData(resListFinal).build();

        return multiChartTableResponse;
    }

    @GetMapping("/resultBm1/{cpu}/{app_name}/{bm_name}")
    public List<ChartsResponse> getAvgResultCPUBMOld(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("bm_name") String bm_name) {
        List<ChartsResponse> listResponse = new ArrayList<>();
        ChartsResponse chartsResponse = null;
        List<AverageResult> list = null;
        String comment = null;
        list = averageResultService.getAvgResultCPUAppBm(cpu, app_name, bm_name);

        if (list == null || list.size()==0)
            return listResponse;

        String appBmCpu = getAppName(app_name) + " - " + bm_name + " - " + cpu;
        String metric = getMetric(app_name.toLowerCase().trim());

        List<String> label = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (AverageResult avgRes : list) {
            label.add(avgRes.getNodes() + " Node");
            data.add(avgRes.getAvgResult());
        }
        if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
            comment = "Higher is better";
        } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
            comment = "Lower is better";
        }

        chartsResponse = ChartsResponse.builder().metric(metric).appCPUName(appBmCpu).dataset(data).labels(label).comment(comment).build();
        listResponse.add(chartsResponse);
        return listResponse;

    }

    @GetMapping("/scalingTable/{cpu}/{app_name}")
    public MultiChartTableResponse getScalingTable(@PathVariable("app_name") String app_name,@PathVariable("cpu") String cpu) {

        MultiChartTableResponse multiChartTableResponse = null;

        List<Map<String, String>> resListFinal = null;
        List<AverageResult> list = null;

        list = averageResultService.getAvgResultCPUApp(cpu, app_name);


        if (list == null || list.size() == 0)
            return multiChartTableResponse;

        Set<Integer> nodes = new LinkedHashSet<>();
        Set<String> cores = new LinkedHashSet<>();
        Set<String> bms = new LinkedHashSet<>();

        for (AverageResult avg : list) {
            nodes.add(avg.getNodes());
            bms.add(avg.getBmName());
            cores.add(String.valueOf(avg.getCores()));
        }

        List<String> bmlist = bms.stream().collect(Collectors.toList());

        List<Map<String, Double>> resList = new ArrayList<>();

        Map<String, Double> res = null;

        List<String> nodesList = new ArrayList<>();
        List<String> coresList = cores.stream().collect(Collectors.toList());

        for (Integer n : nodes) {
            res = new LinkedHashMap<>();
            nodesList.add(n.toString());
            for (AverageResult a : list) {
                if (a.getNodes() == n) {

                    res.put(a.getBmName(), a.getAvgResult());
                }
            }
            resList.add(res);
        }


        List<Map<String, Double>> newResList = new ArrayList<>();

        for (Map<String, Double> e : resList) {

            Map<String, Double> map = new LinkedHashMap<>();
            for (String bm : bmlist) {
                if (!e.containsKey(bm)) {
                    map.put(bm, 0.0);
                } else {
                    map.put(bm, e.get(bm));
                }
            }
            newResList.add(map);

        }


        resListFinal = new ArrayList<>();
        if(nodesList.size()>1) {
            Map<String, Double> firstResult = new LinkedHashMap<>();
            firstResult.putAll(newResList.get(0));

            Map<String, String> temp = new LinkedHashMap<>();
            temp.put("Nodes", nodesList.get(0));
            temp.put("Cores", coresList.get(0));

            for (Map.Entry<String, Double> d : newResList.get(0).entrySet()) {

                if (Double.compare(d.getValue(), 0.0) > 0)
                    temp.put(d.getKey(), "1.0");
            }
            resListFinal.add(temp);

            newResList.remove(0);

            int i = 1;

            for (Map<String, Double> lis : newResList) {

                temp = new LinkedHashMap<>();
                temp.put("Nodes", nodesList.get(i));
                temp.put("Cores", coresList.get(i));
                for (Map.Entry<String, Double> d : lis.entrySet()) {
                    if (Double.compare(firstResult.get(d.getKey()), 0.0) > 0) {
                        if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
                            double d1 = util.round(firstResult.get(d.getKey()) / d.getValue(), 3);
                            temp.put(d.getKey(), String.valueOf(d1));

                        } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                            double d1 = util.round(d.getValue() / firstResult.get(d.getKey()), 3);
                            temp.put(d.getKey(), String.valueOf(d1));
                        }
                    }

                }
                resListFinal.add(temp);
                i++;
            }

            List<String> bmlistFinal = new ArrayList<>();

            for (Map.Entry<String, String> bm : resListFinal.get(0).entrySet()) {
                bmlistFinal.add(bm.getKey());
            }

            multiChartTableResponse = MultiChartTableResponse.builder().appName(getAppName(app_name)).label(bmlistFinal).resultData(resListFinal).build();
        }
        return multiChartTableResponse;
    }








    @GetMapping("/result/{app_name}")
    public List<MultiChartResponseOld> getAvgBySelectedCPUChartOld(@PathVariable("app_name") String app_name, String[] cpuList) {
        List<String> cpus = Arrays.asList(cpuList);
        String metric = getMetric(app_name.toLowerCase().trim());
        String comment = "";
        List<MultiChartResponseOld> resultList = new ArrayList<>();
        MultiChartResponseOld mlr = new MultiChartResponseOld();
        List<AverageResult> list = null;
        list = averageResultService.getBySelectedCPUApp(app_name, cpus);

        if (list == null || list.size()==0)
            return resultList;

        Set<String> cpu = new LinkedHashSet<>();
        Set<String> bms = new LinkedHashSet<>();

        for (AverageResult avg : list) {
            cpu.add(avg.getCpuSku());
            bms.add(avg.getBmName());
        }
        if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
            comment = "Higher is better";
        } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
            comment = "Lower is better";
        }

        List<String> label = new ArrayList<>(bms);
        mlr.setLabels(label);
        mlr.setMetric(metric);
        mlr.setAppName(getAppName(list.get(0).getAppName()));
        mlr.setComment(comment);
        List<DatasetOld> d = new ArrayList<>();
        for (String c : cpu) {

            DatasetOld data = new DatasetOld();
            List<Double> res = new ArrayList<>();
            for (AverageResult avg : list) {
                if (avg.getCpuSku().equals(c)) {

                    res.add(avg.getAvgResult());
                }
            }
            data.setCpuName(c);
            data.setValue(res);
            d.add(data);
        }
        mlr.setDatasets(d);
        resultList.add(mlr);
        if (resultList == null) {
            return Collections.emptyList();
        }
        return resultList;
    }

    @GetMapping("/scalingTableOld/{cpu}/{app_name}")
    public MultiChartTableResponse getScalingTableOld(@PathVariable("app_name") String app_name,@PathVariable("cpu") String cpu) {

        MultiChartTableResponse multiChartTableResponse = null;

        List<Map<String, String>> resListFinal = null;
        List<AverageResult> list = null;

        list = averageResultService.getAvgResultCPUApp(cpu, app_name);


        if (list == null || list.size() == 0)
            return multiChartTableResponse;

        Set<Integer> nodes = new LinkedHashSet<>();
        Set<String> bms = new LinkedHashSet<>();

        for (AverageResult avg : list) {
            nodes.add(avg.getNodes());
            bms.add(avg.getBmName());
        }

        List<String> bmlist = bms.stream().collect(Collectors.toList());
        List<Integer> nodelist = nodes.stream().collect(Collectors.toList());

        List<Map<String, Double>> resList = new ArrayList<>();

        Map<String, Double> res = null;

        List<String> nodesList = new ArrayList<>();

        for (Integer n : nodes) {
            res = new LinkedHashMap<>();
            nodesList.add(n.toString());
            for (AverageResult a : list) {
                if (a.getNodes() == n) {

                    res.put(a.getBmName(), a.getAvgResult());
                }
            }
            resList.add(res);
        }


        List<Map<String, Double>> newResList = new ArrayList<>();

        for (Map<String, Double> e : resList) {

            Map<String, Double> map = new LinkedHashMap<>();
            for (String bm : bmlist) {
                if (!e.containsKey(bm)) {
                    map.put(bm, 0.0);
                } else {
                    map.put(bm, e.get(bm));
                }
            }
            newResList.add(map);

        }


        resListFinal = new ArrayList<>();
        if(nodesList.size()>1) {
            Map<String, Double> firstResult = new LinkedHashMap<>();
            firstResult.putAll(newResList.get(0));

            Map<String, String> temp = new LinkedHashMap<>();
            temp.put("", nodesList.get(0));

            for (Map.Entry<String, Double> d : newResList.get(0).entrySet()) {

                if (Double.compare(d.getValue(), 0.0) > 0)
                    temp.put(d.getKey(), "1.0");
            }
            resListFinal.add(temp);

            newResList.remove(0);

            int i = 1;

            for (Map<String, Double> lis : newResList) {

                temp = new LinkedHashMap<>();
                temp.put("", nodesList.get(i));
                for (Map.Entry<String, Double> d : lis.entrySet()) {
                    if (Double.compare(firstResult.get(d.getKey()), 0.0) > 0) {
                        if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
                            double d1 = util.round(firstResult.get(d.getKey()) / d.getValue(), 3);
                            temp.put(d.getKey(), String.valueOf(d1));

                        } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                            double d1 = util.round(d.getValue() / firstResult.get(d.getKey()), 3);
                            temp.put(d.getKey(), String.valueOf(d1));
                        }
                    }

                }
                resListFinal.add(temp);
                i++;
            }

            List<String> bmlistFinal = new ArrayList<>();

            for (Map.Entry<String, String> bm : resListFinal.get(0).entrySet()) {
                bmlistFinal.add(bm.getKey());
            }

            multiChartTableResponse = MultiChartTableResponse.builder().appName(getAppName(app_name)).label(bmlistFinal).resultData(resListFinal).build();
        }
        return multiChartTableResponse;
    }

    @GetMapping("/resultApp/{cpu}/{app_name}")
    public ScatterTableResponse getAvgResultCPUApp(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name) {
        ScatterTableResponse scatterTableResponse = null;
        List<AverageResult> list = null;
        String comment = null;
        list = averageResultService.getAvgResultCPUApp(cpu, app_name);

        if (list == null || list.size()==0)
            return scatterTableResponse;


        Set<String> cpus = new LinkedHashSet<>();
        Set<String> bms = new LinkedHashSet<>();
        Set<String> nodes = new LinkedHashSet<>();
        nodes.add("Nodes/Benchmark");

        for (AverageResult avg : list) {
            cpus.add(avg.getCpuSku());
            bms.add(avg.getBmName());
            nodes.add(String.valueOf(avg.getNodes()));
        }

        List<Map<Integer, Double>> listMap = new ArrayList<>();
        Map<Integer, Double> res = null;

        List<String> bmList = bms.stream().collect(Collectors.toList());

        for (String b : bmList) {
            res = new LinkedHashMap<>();
            for (AverageResult a : list) {
                if (a.getBmName().equals(b)) {

                    res.put(a.getNodes(), a.getAvgResult());
                }
            }
            listMap.add(res);
        }


        List<Map<String, String>> resList = new ArrayList<>();


        Map<String, String> temp = null;

        int i=0;
        if(listMap.get(0).size() >2) {
            for (Map<Integer, Double> m : listMap) {
                double firstResult = m.entrySet().iterator().next().getValue();
                temp = new LinkedHashMap<>();
                temp.put("Nodes/Benchmark", bmList.get(i));
                temp.put(m.entrySet().iterator().next().getKey().toString(), "1.0");
                m.remove(m.entrySet().iterator().next().getKey());

                for (Map.Entry<Integer, Double> entry : m.entrySet()) {
                    double d1;
                    if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
                        d1 = util.round(firstResult / entry.getValue(), 3);
                        temp.put(entry.getKey().toString(), String.valueOf(d1));

                    } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                        d1 = util.round(entry.getValue() / firstResult, 3);
                        temp.put(entry.getKey().toString(), String.valueOf(d1));
                    }

                }
                resList.add(temp);
                i++;
            }
        }

        scatterTableResponse = ScatterTableResponse.builder().nodes(nodes).appName(app_name).resultData(resList).build();
        return scatterTableResponse;
    }


}
