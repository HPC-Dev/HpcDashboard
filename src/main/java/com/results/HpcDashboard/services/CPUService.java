package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.CPUDto;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CPUService {
    @Autowired
    CPURepo cpuRepo;

    @Autowired
    Util util;

    @PersistenceContext
    private EntityManager entityManager;

    public void insertCPU(String[] resultData){
        CPU cpu = CPU.builder().cpuGeneration(resultData[1]).maxDdrFreq(resultData[9]).ddrChannels(Integer.valueOf(resultData[8])).l3Cache(resultData[7]).peakFreq(resultData[6]).baseFreq(resultData[5]).cores(Integer.valueOf(resultData[4])).tdp(resultData[3]).cpuSku(resultData[2].trim()).cpuManufacturer(resultData[0].trim()).build();
        cpuRepo.save(cpu);
    }

    public List<CPU> getAllCPUData(){
        Iterable<CPU> cpus = cpuRepo.findAll();
        List<CPU> list = null;
        list = StreamSupport
                .stream(cpus.spliterator(), false)
                .collect(Collectors.toList());

        if(list ==null){
            return Collections.emptyList();
        }

        return list;
    }

    public List<CPUDto> getAllCPUsCores(){
        return util.findAllCPUs(entityManager);
    }

    public List<String> getAllCPUs(){
        return cpuRepo.findAllCPUs();
    }

    public void insertCPUCsv(List<CPU> cpus){
        for(CPU cpu: cpus) {
            cpuRepo.save(cpu);
        }
    }


}
