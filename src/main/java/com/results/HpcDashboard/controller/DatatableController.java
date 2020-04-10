package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Applications;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.paging.Page;
import com.results.HpcDashboard.paging.PagingRequest;
import com.results.HpcDashboard.repo.ApplicationsRepo;
import com.results.HpcDashboard.services.DatatableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("resultAjax")
public class DatatableController {

    private final DatatableService datatableService;

    @Autowired
    public DatatableController(DatatableService datatableService) {
        this.datatableService = datatableService;
    }

    @PostMapping
    public Page<Result> list(@RequestBody PagingRequest pagingRequest) {
        return datatableService.getResults(pagingRequest);
    }

}
