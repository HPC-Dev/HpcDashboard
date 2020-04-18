package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.CPU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CPURepo extends JpaRepository<CPU,Integer> {

    public static final String FIND_CPU_LIST = "select cpu_sku from cpu_info ORDER BY cpu_sku ASC";

    @Query(value = FIND_CPU_LIST, nativeQuery = true)
    public List<String> findAllCPUs();
}
