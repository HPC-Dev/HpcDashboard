package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepo extends JpaRepository<Application,Integer> {

    public static final String FIND_APP_NAME = "select app_name from applications";

    @Query(value = FIND_APP_NAME, nativeQuery = true)
    public List<String> findAllApps();
}
