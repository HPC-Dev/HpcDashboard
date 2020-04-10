package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    @Autowired
    ResultRepo resultRepo;

    public void insertResult(Result result){
        resultRepo.save(result);
    }

    public List<Result> getAllResults(){
        return resultRepo.findAll();
    }
}
