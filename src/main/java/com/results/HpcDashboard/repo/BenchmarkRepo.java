package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Benchmark;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface BenchmarkRepo extends DataTablesRepository<Benchmark,Integer> {
}