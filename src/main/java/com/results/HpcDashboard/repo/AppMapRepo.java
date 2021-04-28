package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.AppMap;
import com.results.HpcDashboard.models.Processor;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppMapRepo extends DataTablesRepository<AppMap,String> {

    public static final String FIND_APP_MAP = "select * from app_map ORDER BY app_name ASC";

    @Query(value = FIND_APP_MAP, nativeQuery = true)
    public List<AppMap> findAllAppMap();
}
