package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Applications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationsRepo extends JpaRepository<Applications,Integer> {
}
