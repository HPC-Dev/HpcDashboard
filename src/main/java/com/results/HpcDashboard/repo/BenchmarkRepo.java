package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Benchmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenchmarkRepo extends JpaRepository<Benchmark,Integer> {
}
