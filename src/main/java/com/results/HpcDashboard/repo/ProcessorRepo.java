package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Processor;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProcessorRepo extends DataTablesRepository<Processor,String> {

    public static final String FIND_CPU_LIST = "select * from processor_info ORDER BY cpu_sku ASC";

    @Query(value = FIND_CPU_LIST, nativeQuery = true)
    public List<Processor> findAllProcessors();
}
