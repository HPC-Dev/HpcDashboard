package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.AppCategory;
import com.results.HpcDashboard.models.AppMap;
import com.results.HpcDashboard.models.Benchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AppCategoryRepo extends JpaRepository<AppCategory,String> {

    public static final String FIND_APP_CATEGORY = "select * from app_category ORDER BY app_name ASC";

    @Query(value = FIND_APP_CATEGORY, nativeQuery = true)
    public List<AppCategory> findAllAppCategory();
}