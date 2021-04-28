package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.MetricMap;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetricMapRepo extends DataTablesRepository<MetricMap,String> {

    public static final String FIND_METRIC_MAP = "select * from metric_map ORDER BY app_name ASC";

    @Query(value = FIND_METRIC_MAP, nativeQuery = true)
    public List<MetricMap> findAllMetric();
}
