package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.paging.Page;
import com.results.HpcDashboard.paging.PagingRequest;
import com.results.HpcDashboard.services.ApplicationDataTableService;
import com.results.HpcDashboard.services.BenchmarkDataTableService;
import com.results.HpcDashboard.services.CPUDataTableService;
import com.results.HpcDashboard.services.ResultDataTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class DatatableRestController {

    private final ResultDataTableService resultDataTableService;

    private final ApplicationDataTableService applicationDataTableService;

    private final BenchmarkDataTableService benchmarkDataTableService;

    private final CPUDataTableService cpuDataTableService;

    @Autowired
    public DatatableRestController(ResultDataTableService resultDataTableService, ApplicationDataTableService applicationDataTableService, BenchmarkDataTableService benchmarkDataTableService, CPUDataTableService cpuDataTableService) {
        this.resultDataTableService = resultDataTableService;
        this.applicationDataTableService = applicationDataTableService;
        this.benchmarkDataTableService = benchmarkDataTableService;
        this.cpuDataTableService = cpuDataTableService;
    }

    @PostMapping("resultAjax")
    public Page<Result> getResults(@RequestBody PagingRequest pagingRequest) {
        return resultDataTableService.getData(pagingRequest);
    }


    @PostMapping("cpuAjax")
    public Page<CPU> getCPUs(@RequestBody PagingRequest pagingRequest) {
        return cpuDataTableService.getData(pagingRequest);
    }

    @PostMapping("applicationAjax")
    public Page<Application> getApplications(@RequestBody PagingRequest pagingRequest) {
        return applicationDataTableService.getData(pagingRequest);
    }

    @PostMapping("benchmarkAjax")
    public Page<BenchmarkDto> getBenchmarks(@RequestBody PagingRequest pagingRequest) {
        return benchmarkDataTableService.getData(pagingRequest);
    }

}
