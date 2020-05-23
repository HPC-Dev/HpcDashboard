package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.Benchmark;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class BenchmarkService {
    @Autowired
    BenchmarkRepo benchmarkrepo;

    @Autowired
    Util util;

    @PersistenceContext
    private EntityManager entityManager;

    public void insertBenchmark(String[] resultData){

    }

    public List<BenchmarkDto> getAllBenchmarks(){
        return util.getBenchmarks(entityManager);
    }

    public void insertBmCsv(List<Benchmark> bms){
        for(Benchmark bm: bms) {
            benchmarkrepo.save(bm);
        }
    }
}
