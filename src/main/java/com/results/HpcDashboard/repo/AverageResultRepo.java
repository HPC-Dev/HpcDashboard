package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.dto.AverageResultId;
import com.results.HpcDashboard.models.AverageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AverageResultRepo extends JpaRepository<AverageResult, AverageResultId> {

    public static final String UPDATE_AVG_RESULT = "UPDATE average_result set avg_result=:avg where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    public static final String DELETE_AVG_RESULT = "DELETE FROM average_result where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    public static final String GET_AVG_RESULT = "SELECT * from average_result where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    public static final String GET_AVG_RESULT_CPU_APP = "SELECT * from average_result where cpu_sku =:cpu_sku and app_name =:app_name";
    public static final String GET_CPU = "select DISTINCT cpu_sku from average_result ORDER BY cpu_sku ASC";
    public static final String GET_APP = "select DISTINCT app_name from average_result ORDER BY app_name ASC";

    @Modifying
    @Query(value = UPDATE_AVG_RESULT, nativeQuery = true)
    void updateAverageResult(String bm_name, String cpu_sku, int nodes, double avg);

    @Modifying
    @Query(value = DELETE_AVG_RESULT, nativeQuery = true)
    void deleteAverageResult(String bm_name, String cpu_sku, int nodes);

    @Query(value = GET_AVG_RESULT, nativeQuery = true)
    AverageResult getAverageResult(String bm_name, String cpu_sku, int nodes);

    @Query(value = GET_AVG_RESULT_CPU_APP, nativeQuery = true)
    List<AverageResult> getAverageResultCPUApp(String cpu_sku, String app_name);

    @Query(value = GET_APP, nativeQuery = true)
    List<String> getAPP();

    @Query(value = GET_CPU, nativeQuery = true)
    List<String> getCPU();
}
