package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ResultRepo extends JpaRepository<Result,Integer> {

    public static final String FIND_RESULTS_APP = "SELECT result FROM results where bm_name=:bm_name";
    public static final String FIND_RESULTS_APP_CPU_NODE = "SELECT result FROM results where bm_name=:bm_name and cpu=:cpu and nodes=:nodes ";

    @Query(value = FIND_RESULTS_APP, nativeQuery = true)
    public List<Double> findresultsByApp(String bm_name);

    @Query(value = FIND_RESULTS_APP_CPU_NODE, nativeQuery = true)
    public List<Double> findresultsByAppCPUNode(String bm_name, String cpu, int nodes);


}
