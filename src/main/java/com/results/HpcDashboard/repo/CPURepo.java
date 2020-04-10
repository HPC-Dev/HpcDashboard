package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Applications;
import com.results.HpcDashboard.models.CPU;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CPURepo extends JpaRepository<CPU,Integer> {
}
