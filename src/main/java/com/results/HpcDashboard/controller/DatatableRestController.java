package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.Benchmark;
import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;


@RestController
@RequestMapping(value = "/datatable")
public class DatatableRestController {

    @Autowired
    ResultRepo resultRepo;

    @Autowired
    ResultService resultService;

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    CPURepo cpuRepo;

    @Autowired
    BenchmarkRepo benchmarkRepo;



    @GetMapping(value = "/dashboard")
    public DataTablesOutput<Result> listResults(@Valid DataTablesInput input) {
        return resultRepo.findAll(input, new DateSpecification(input));
    }

    private static class DateSpecification implements Specification<Result> {
        private final Date startDate;
        private final Date endDate;

        DateSpecification(DataTablesInput input) {
            String dateFilter = input.getColumn("timeStamp").getSearch().getValue();
            if (!hasText(dateFilter)) {
                startDate = endDate = null;
                return;
            }
            String[] bounds = dateFilter.split(",");
            startDate = getValue(bounds, 0);
            endDate = getValue(bounds, 1);
        }

        private Date getValue(String[] bounds, int index) {
            if (bounds.length > index && hasText(bounds[index])) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd")
                            .parse(bounds[index]);
                } catch (NumberFormatException | ParseException e) {
                    return null;
                }
            }
            return null;
        }

        @Override
        public javax.persistence.criteria.Predicate toPredicate(Root<Result> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            Expression<Date> date = root.get("timeStamp").as(Date.class);
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(date, startDate, endDate);
            }
             else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(date, startDate);
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(date, endDate);
            }
            else {
                return criteriaBuilder.conjunction();
            }
        }
    }


    @GetMapping(value = "apps")
    public DataTablesOutput<Application> listApplications(@Valid DataTablesInput input) {
        DataTablesOutput<Application> applications = applicationRepo.findAll(input);
        return applications;
    }

    @GetMapping(value = "cpu")
    public DataTablesOutput<CPU> listCPU(@Valid DataTablesInput input) {
        DataTablesOutput<CPU> cpu = cpuRepo.findAll(input);
        return cpu;
    }

    @GetMapping(value = "bms")
    public DataTablesOutput<Benchmark> listBms(@Valid DataTablesInput input) {
        DataTablesOutput<Benchmark> cpu = benchmarkRepo.findAll(input);
        return cpu;
    }

}
