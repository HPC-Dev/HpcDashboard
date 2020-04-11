package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.repo.CPURepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CPUService {
    @Autowired
    CPURepo cpuRepo;

    public void insertCPU(String[] resultData){
        CPU cpu = CPU.builder().cpu_generation(resultData[1]).max_ddr_freq(resultData[9]).ddr_channels(Integer.valueOf(resultData[8])).l3_cache(Integer.valueOf(resultData[7])).peak_freq(resultData[6]).base_freq(resultData[5]).cores(Integer.valueOf(resultData[4])).tdp(resultData[3]).cpu_sku(resultData[2]).cpu_manufacturer(resultData[0]).build();
        cpuRepo.save(cpu);
    }

    public List<CPU> getAllCPUs(){
        return cpuRepo.findAll();
    }
}
