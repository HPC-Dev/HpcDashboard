package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepo extends JpaRepository<Result,Integer> {
}
