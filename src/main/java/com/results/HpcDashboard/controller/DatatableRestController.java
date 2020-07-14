package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.Benchmark;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/datatable")
public class DatatableRestController {

    @Autowired
    ResultRepo resultRepo;

    @Autowired
    ResultService resultService;

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    CPURepo cpuRepo;

    @Autowired
    BenchmarkRepo benchmarkRepo;


    @GetMapping(value = "dashboard")
    public DataTablesOutput<Result> listResults(@Valid DataTablesInput input) {

//            Column column = input.getColumn("timeStamp");
        DataTablesOutput<Result> result = resultRepo.findAll(input);
        return result;
    }

    @RequestMapping(value = { "resultListbyStartEndDate" }, method = RequestMethod.GET)
    public DataTablesOutput<Result> listbyStartEndDate(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws ParseException {
        DataTablesOutput<Result> result = new DataTablesOutput<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = df.parse(startDate);
        Date date2 = df.parse(endDate);
        List<Result> results = resultService.findByStartEndDate(date1,date2);
        result.setData(results);
        result.setRecordsTotal(results.size());
        result.setRecordsFiltered(results.size());
        result.setDraw(1);
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
