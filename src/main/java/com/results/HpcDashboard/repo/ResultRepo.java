package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ResultRepo extends JpaRepository<Result,Integer> {

    public static final String FIND_RESULTS = "SELECT result FROM results where bm_name=:bm_name";

    @Query(value = FIND_RESULTS, nativeQuery = true)
    public List<BigDecimal> findresults(String bm_name);

}
