package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.repo.AppCategoryRepo;
import com.results.HpcDashboard.repo.AppMapRepo;
import com.results.HpcDashboard.repo.ProcessorRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/helper")
public class HelperRestController {


    @Autowired
    Util util;

    @Autowired
    AppMapRepo appMapRepo;

    @Autowired
    ProcessorRepo processorRepo;

    @Autowired
    AppCategoryRepo appCategoryRepo;


    public String getLowerHigher(String app){

        List<AppMap> appMaps = appMapRepo.findAllAppMap();

        String appStatus = appMaps.stream()
                .filter(appMap -> app.equals(appMap.getAppName()))
                .findAny()
                .orElse(null).getStatus();
        return appStatus;

    }

    @GetMapping("/appMetricStatus")
    public List<AppMap> getAppMap(){

        List<AppMap> list = null;
        list = appMapRepo.findAllAppMap();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }


    @GetMapping("/appCategory")
    public List<AppCategory> getAppCategory(){

        List<AppCategory> list = null;
        list = appCategoryRepo.findAllAppCategory();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }

    @GetMapping("/procStatus")
    public List<Processor> getProc(){

        List<Processor> list = null;
        list = processorRepo.findAllProcessors();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }


}
