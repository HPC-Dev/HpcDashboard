package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ResultService {
    @Autowired
    ResultRepo resultRepo;

    public void insertResult(String[] resultData){
        Result result = Result.builder().job_id(resultData[0]).app_name(resultData[1]).bm_name(resultData[2]).nodes(Integer.valueOf(resultData[3])).cores(Integer.valueOf(resultData[4])).node_name(resultData[5].replaceAll("\\\\,",",")).result(new BigDecimal(resultData[6])).cpu(resultData[7].toUpperCase()).build();
        resultRepo.save(result);
    }

    public List<Result> getAllResults(){
        return resultRepo.findAll();
    }
}
