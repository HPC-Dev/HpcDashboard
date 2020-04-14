package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.dto.AverageResultId;
import com.results.HpcDashboard.models.AverageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface AverageResultRepo extends JpaRepository<AverageResult, AverageResultId> {

    public static final String UPDATE_AVG_RESULT = "UPDATE average_result set avg_result=:avg where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    public static final String DELETE_AVG_RESULT = "DELETE FROM average_result where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    public static final String GET_AVG_RESULT = "SELECT * from average_result where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    @Modifying
    @Query(value = UPDATE_AVG_RESULT, nativeQuery = true)
    void updateAverageResult(String bm_name, String cpu_sku, int nodes, double avg);

    @Modifying
    @Query(value = DELETE_AVG_RESULT, nativeQuery = true)
    void deleteAverageResult(String bm_name, String cpu_sku, int nodes);

    @Query(value = GET_AVG_RESULT, nativeQuery = true)
    AverageResult getAverageResult(String bm_name, String cpu_sku, int nodes);
}
