package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.dto.AverageResultId;
import com.results.HpcDashboard.models.AverageResult;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AverageResultRepo extends DataTablesRepository<AverageResult, AverageResultId> {

    public static final String UPDATE_AVG_RESULT = "UPDATE average_result set avg_result=:avg, coefficient_of_variation=:cv, run_count=:count where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    public static final String DELETE_AVG_RESULT = "DELETE FROM average_result where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    public static final String GET_AVG_RESULT = "SELECT * from average_result where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes";
    public static final String GET_AVG_RESULT_CPU_APP = "SELECT * from average_result where cpu_sku =:cpu_sku and app_name =:app_name ORDER BY bm_name";
    public static final String GET_AVG_RESULT_CPU_APP_BM = "SELECT * from average_result where cpu_sku =:cpu_sku and app_name =:app_name and bm_name =:bm_name";
    public static final String GET_AVG_RESULT_CPU_APP_NODE = "SELECT * from average_result where cpu_sku =:cpu_sku and app_name =:app_name and nodes =:nodes";
    public static final String GET_CPU = "select DISTINCT cpu_sku from average_result where app_name=:appName and nodes=1 ORDER BY cpu_sku ASC";
    public static final String GET_CPU_RES = "select DISTINCT cpu_sku from average_result ORDER BY cpu_sku ASC";
    public static final String GET_APP = "select DISTINCT LOWER(app_name) from average_result ORDER BY app_name ASC";
    public static final String GET_APP_CPU = "select DISTINCT LOWER(app_name) from average_result where cpu_sku=:cpu ORDER BY app_name ASC;";
    public static final String GET_SELECTED_CPU_RES_BY_AVG = "select * from average_result where app_name= :app_name and cpu_sku IN (:cpus) and nodes =1 ORDER BY bm_name";
    public static final String GET_COMP_CPU_RES = "select * from average_result where app_name= :app_name and cpu_sku =:cpu and nodes =1 ORDER BY bm_name;";
    public static final String GET_SELECTED_BM_CPU = "select DISTINCT bm_name from average_result where app_name=:app_name and cpu_sku=:cpu ORDER BY bm_name ASC";
    public static final String GET_SELECTED_BM = "select DISTINCT bm_name from average_result where app_name=:app_name ORDER BY bm_name ASC";
    public static final String Job_EXITS ="select count(*) from results where job_id=:jobId";

    public static final String GET_SELECTED_CPU_RES_BY_AVG_New_ASC = "select * from average_result where app_name= :app_name and cpu_sku IN (:cpus) and nodes =1 ORDER BY bm_name,avg_result";

    public static final String GET_SELECTED_CPU_RES_BY_AVG_New_DESC = "select * from average_result where app_name= :app_name and cpu_sku IN (:cpus) and nodes =1 ORDER BY bm_name,avg_result DESC";

    @Modifying
    @Query(value = UPDATE_AVG_RESULT, nativeQuery = true)
    void updateAverageResult(String bm_name, String cpu_sku, int nodes, double avg, double cv, int count);

    @Modifying
    @Query(value = DELETE_AVG_RESULT, nativeQuery = true)
    void deleteAverageResult(String bm_name, String cpu_sku, int nodes);

    @Query(value = GET_AVG_RESULT, nativeQuery = true)
    AverageResult getAverageResult(String bm_name, String cpu_sku, int nodes);

    @Query(value = GET_AVG_RESULT_CPU_APP, nativeQuery = true)
    List<AverageResult> getAverageResultCPUApp(String cpu_sku, String app_name);

    @Query(value = GET_AVG_RESULT_CPU_APP_BM, nativeQuery = true)
    List<AverageResult> getAverageResultCPUAppBm(String cpu_sku, String app_name, String bm_name);

    @Query(value = GET_AVG_RESULT_CPU_APP_NODE, nativeQuery = true)
    List<AverageResult> getAverageResultCPUAppNode(String cpu_sku, String app_name, int nodes);

    @Query(value = GET_APP, nativeQuery = true)
    List<String> getAPP();

    @Query(value = GET_APP_CPU, nativeQuery = true)
    List<String> getAPP(String cpu);

    @Query(value = GET_CPU, nativeQuery = true)
    List<String> getCPU(String appName);

    @Query(value = GET_CPU_RES, nativeQuery = true)
    List<String> getCPU();

    @Query(nativeQuery =true,value = GET_SELECTED_CPU_RES_BY_AVG)
    List<AverageResult> findBySelectedCPUApp(String app_name, List<String> cpus);


    @Query(nativeQuery =true,value = GET_SELECTED_CPU_RES_BY_AVG_New_ASC)
    List<AverageResult> findBySelectedCPUAppAsc(String app_name, List<String> cpus);


    @Query(nativeQuery =true,value = GET_SELECTED_CPU_RES_BY_AVG_New_DESC)
    List<AverageResult> findBySelectedCPUAppDesc(String app_name, List<String> cpus);



    @Query(nativeQuery =true,value = GET_COMP_CPU_RES)
    List<AverageResult> findCompDataBySelectedCPU(String app_name, String cpu);

    @Query(value = GET_SELECTED_BM_CPU, nativeQuery = true)
    List<String> getSelectedBm(String app_name, String cpu);

    @Query(value = GET_SELECTED_BM, nativeQuery = true)
    List<String> getSelectedBm(String app_name);

    @Query(value = Job_EXITS, nativeQuery = true)
    int getJobExists(String jobId);

}
