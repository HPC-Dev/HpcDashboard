package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepo extends JpaRepository<Application,Integer> {
}
