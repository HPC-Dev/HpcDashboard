package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.AppCategory;
import com.results.HpcDashboard.models.Benchmark;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.services.AppCategoryService;
import com.results.HpcDashboard.services.BenchmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
@Controller
public class AppCategoryController {

    @Autowired
    AppCategoryService appCategoryService;

    @PostMapping(value = "/appCategoryJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertAppCategoryJson(@RequestBody List<AppCategory> appCategories) {
        if(appCategories != null || appCategories.size() > 0 )
            appCategoryService.insertAppCategory(appCategories);

        return new ResponseEntity("Success!",HttpStatus.OK);
    }

}

