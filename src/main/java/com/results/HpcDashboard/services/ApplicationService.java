package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.repo.AppMapRepo;
import com.results.HpcDashboard.repo.ApplicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ApplicationService {
    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    AppMapRepo appMapRepo;

    public void insertApplication(String[] resultData){
        Application app = new Application();
        applicationRepo.save(app);
    }



    public List<Application> getAllApplications(){
        Iterable<Application> apps = applicationRepo.findAll();
        List<Application> list = null;
        list = StreamSupport
                .stream(apps.spliterator(), false)
                .collect(Collectors.toList());

        if(list ==null){
            return Collections.emptyList();
        }

        return list;
    }


    public List<String>  getAllAppNames(){
        return applicationRepo.findAllApps();
    }

    public String getMetric(String appName){
        String metric = "";
        metric = applicationRepo.getAppMetric(appName);
        if(metric == null){
            return "";
        }
        return metric;
    }

    public void insertAppCsv(List<Application> apps){
        for(Application app: apps) {
            applicationRepo.save(app);
        }
    }


    public void insertAppMapCSV(List<AppMap> appMaps) {
        for(AppMap appMap: appMaps) {
            appMapRepo.save(appMap);
        }

    }

}
