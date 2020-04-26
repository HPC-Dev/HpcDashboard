package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Application;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepo extends DataTablesRepository<Application,Integer> {

    public static final String FIND_APP_NAME = "select app_name from applications ORDER BY app_name ASC";
    public static final String FIND_METRIC = "select precision_info from applications where app_name=:appName";

    @Query(value = FIND_APP_NAME, nativeQuery = true)
    public List<String> findAllApps();

    @Query(value = FIND_METRIC, nativeQuery = true)
    public String getAppMetric(String appName);
}
