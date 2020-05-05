package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Result;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepo extends DataTablesRepository<Result, String> {

    public static final String FIND_RESULTS_APP_CPU_NODE = "SELECT result FROM results where bm_name=:bm_name and cpu=:cpu and nodes=:nodes ";
    public static final String GET_CPU = "select DISTINCT cpu from results ORDER BY cpu ASC";
    public static final String GET_APP = "select DISTINCT app_name from results ORDER BY app_name ASC";
    public static final String GET_BM = "select DISTINCT bm_name from results ORDER BY bm_name ASC";
    public static final String GET_Nodes = "select DISTINCT nodes from results ORDER BY nodes ASC";


    @Query(value = FIND_RESULTS_APP_CPU_NODE, nativeQuery = true)
    public List<Double> findresultsByAppCPUNode(String bm_name, String cpu, int nodes);

    @Query(value = GET_CPU, nativeQuery = true)
    List<String> getCpu();

    @Query(value = GET_APP, nativeQuery = true)
    List<String> getApp();

    @Query(value = GET_BM, nativeQuery = true)
    List<String> getBm();

    @Query(value = GET_Nodes, nativeQuery = true)
    List<Integer> getNodes();

}
