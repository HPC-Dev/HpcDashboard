package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.AppMap;
import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.MetricMap;
import com.results.HpcDashboard.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;


    @GetMapping("/application")
    public String showApplication() {
        return "application";
    }


    @PostMapping(value = "/appJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertAppJson(@RequestBody List<Application> applications) {
        if(applications != null || applications.size() > 0 )
            applicationService.insertAppCsv(applications);

        return new ResponseEntity("Success!",HttpStatus.OK);
    }

    @PostMapping(value = "/appMapJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertappMap(@RequestBody List<AppMap> appMaps) {
        if(appMaps != null || appMaps.size() > 0 )
            applicationService.insertAppMapCSV(appMaps);
        return new ResponseEntity("Success!",HttpStatus.OK);
    }

    @PostMapping(value = "/metricMapJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertMetricMap(@RequestBody List<MetricMap> metricMaps) {
        if(metricMaps != null || metricMaps.size() > 0 )
            applicationService.insertMetricMapCSV(metricMaps);
        return new ResponseEntity("Success!",HttpStatus.OK);
    }

}
