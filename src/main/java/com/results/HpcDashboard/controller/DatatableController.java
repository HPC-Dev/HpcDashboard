package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.paging.Page;
import com.results.HpcDashboard.paging.PagingRequest;
import com.results.HpcDashboard.services.ApplicationService;
import com.results.HpcDashboard.services.BenchmarkService;
import com.results.HpcDashboard.services.CPUService;
import com.results.HpcDashboard.services.ResultDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class DatatableController {

    private final ResultDashboardService resultDashboardService;

    private final ApplicationService applicationService;

    private final BenchmarkService benchmarkService;

    private final CPUService cpuService;

    @Autowired
    public DatatableController(ResultDashboardService resultDashboardService, ApplicationService applicationService, BenchmarkService benchmarkService, CPUService cpuService ) {
        this.resultDashboardService = resultDashboardService;
        this.applicationService = applicationService;
        this.benchmarkService = benchmarkService;
        this.cpuService = cpuService;
    }

    @PostMapping("resultAjax")
    public Page<Result> getResults(@RequestBody PagingRequest pagingRequest) {
        return resultDashboardService.getData(pagingRequest);
    }


    @PostMapping("cpuAjax")
    public Page<CPU> getCPUs(@RequestBody PagingRequest pagingRequest) {
        return cpuService.getData(pagingRequest);
    }

    @PostMapping("applicationAjax")
    public Page<Application> getApplications(@RequestBody PagingRequest pagingRequest) {
        return applicationService.getData(pagingRequest);
    }

    @PostMapping("benchmarkAjax")
    public Page<BenchmarkDto> getBenchmarks(@RequestBody PagingRequest pagingRequest) {
        return benchmarkService.getData(pagingRequest);
    }

}
