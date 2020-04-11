package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BenchmarkService {
    @Autowired
    BenchmarkService benchmarkService;

    @Autowired
    Util util;

    @PersistenceContext
    private EntityManager entityManager;

    public void insertBenchmark(String[] resultData){

    }

    public List<BenchmarkDto> getAllBenchmarks(){
        return util.getBenchmarks(entityManager);
    }
}
