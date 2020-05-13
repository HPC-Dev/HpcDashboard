package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.partComparision.CompareResult;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.services.ResultService;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/res")
@RestController
public class ResultFormRestController {

    @Autowired
    ResultService resultService;

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    Util util;

    @Autowired
    ResultRepo resultRepo;

    @PostMapping(value = "/resultJson")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Result> insertResult(@RequestBody List<Result> results) {

        if(results != null || results.size() > 0 )
        resultService.insertResultCsv(results);
        return results;
    }

    @GetMapping(value = "/resultJson")
    public List<Result> getResults() {
        List<Result> list = resultService.getAllResults();
        return list;
    }

}
