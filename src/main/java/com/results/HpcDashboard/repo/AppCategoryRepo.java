package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.AppCategory;
import com.results.HpcDashboard.models.Benchmark;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppCategoryRepo extends JpaRepository<AppCategory,String> {
}