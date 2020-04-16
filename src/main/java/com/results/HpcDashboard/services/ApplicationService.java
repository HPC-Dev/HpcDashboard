package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.ResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    ApplicationRepo applicationRepo;

    public void insertApplication(String[] resultData){
        Application app = new Application();
        applicationRepo.save(app);
    }

    public List<Application> getAllApplications(){
        return applicationRepo.findAll();
    }


    public List<String>  getAllAppNames(){
        return applicationRepo.findAllApps();
    }
}
