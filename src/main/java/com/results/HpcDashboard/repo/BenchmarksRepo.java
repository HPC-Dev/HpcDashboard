package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Benchmarks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenchmarksRepo extends JpaRepository<Benchmarks,Integer> {
}
