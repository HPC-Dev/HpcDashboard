package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.CPUDto;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CPUService {
    @Autowired
    CPURepo cpuRepo;

    @Autowired
    Util util;

    @PersistenceContext
    private EntityManager entityManager;

    public void insertCPU(String[] resultData){
        CPU cpu = CPU.builder().cpu_generation(resultData[1]).max_ddr_freq(resultData[9]).ddr_channels(Integer.valueOf(resultData[8])).l3_cache(Integer.valueOf(resultData[7])).peak_freq(resultData[6]).base_freq(resultData[5]).cores(Integer.valueOf(resultData[4])).tdp(resultData[3]).cpu_sku(resultData[2].toUpperCase()).cpu_manufacturer(resultData[0]).build();
        cpuRepo.save(cpu);
    }

    public List<CPU> getAllCPUData(){
        return cpuRepo.findAll();
    }

    public List<CPUDto> getAllCPUsCores(){
        return util.findAllCPUs(entityManager);
    }

    public List<String> getAllCPUs(){
        return cpuRepo.findAllCPUs();
    }


}
