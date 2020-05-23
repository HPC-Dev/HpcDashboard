package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.Benchmark;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.repo.ResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/datatable")
public class DatatableRestController {

    @Autowired
    ResultRepo resultRepo;

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    CPURepo cpuRepo;

    @Autowired
    BenchmarkRepo benchmarkRepo;


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

    @GetMapping(value = "bms")
    public DataTablesOutput<Benchmark> listBms(@Valid DataTablesInput input) {
        DataTablesOutput<Benchmark> cpu = benchmarkRepo.findAll(input);
        return cpu;
    }

}
