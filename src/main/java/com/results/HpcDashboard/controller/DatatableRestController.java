package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.paging.Page;
import com.results.HpcDashboard.paging.PagingRequest;
import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.services.BenchmarkDataTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/datatable")
public class DatatableRestController {

    private final BenchmarkDataTableService benchmarkDataTableService;


    @Autowired
    ResultRepo resultRepo;

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    CPURepo cpuRepo;


    @Autowired
    public DatatableRestController(BenchmarkDataTableService benchmarkDataTableService) {
        this.benchmarkDataTableService = benchmarkDataTableService;

    }

    @PostMapping("benchmarkAjax")
    public Page<BenchmarkDto> getBenchmarks(@RequestBody PagingRequest pagingRequest) {
        return benchmarkDataTableService.getData(pagingRequest);
    }

    @GetMapping(value = "dashboard")
    public DataTablesOutput<Result> listResults(@Valid DataTablesInput input) {
        DataTablesOutput<Result> result = resultRepo.findAll(input);
        return result;
    }


    @GetMapping(value = "apps")
    public DataTablesOutput<Application> listApplications(@Valid DataTablesInput input) {
        DataTablesOutput<Application> applications = applicationRepo.findAll(input);
        return applications;
    }

    @GetMapping(value = "cpu")
    public DataTablesOutput<CPU> listCPU(@Valid DataTablesInput input) {
        DataTablesOutput<CPU> cpu = cpuRepo.findAll(input);
        return cpu;
    }

}
