package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.ApplicationComparators;
import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.paging.*;

import com.results.HpcDashboard.repo.ApplicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ApplicationService implements DataTableService<Application> {

    @Autowired
    ApplicationRepo applicationRepo;
    private static final Comparator<Application> EMPTY_COMPARATOR = (e1, e2) -> 0;

    public Page<Application> getData(PagingRequest pagingRequest)  {
        List<Application> applications = applicationRepo.findAll();
        return getPage(applications, pagingRequest);
    }

    public Page<Application> getPage(List<Application> applications, PagingRequest pagingRequest) {
        List<Application> filtered = applications.stream()
                .sorted(sortData(pagingRequest))
                .filter(filterData(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        long count = applications.stream()
                .filter(filterData(pagingRequest))
                .count();

        Page<Application> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    public Predicate<Application> filterData(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return application -> true;
        }

        String value = pagingRequest.getSearch()
                .getValue();


        return application -> application.getApp_name()
                .toLowerCase()
                .contains(value)
                || application.getApp_id()
                .toLowerCase()
                .contains(value)
                || application.getApp_verion()
                .toLowerCase()
                .contains(value)
                || application.getBinary_info()
                .toLowerCase()
                .contains(value)
                || application.getComp_flags()
                .toLowerCase()
                .contains(value)
                || application.getComp_name()
                .toLowerCase()
                .contains(value)
                || application.getLib_name()
                .toLowerCase()
                .contains(value)
                || application.getLib_flags()
                .toLowerCase()
                .contains(value)
                || application.getApp_bound()
                .toLowerCase()
                .contains(value)
                || application.getPrecision_info()
                .toLowerCase()
                .contains(value)
                || application.getLib_ver()
                .toLowerCase()
                .contains(value);
    }

    public Comparator<Application> sortData(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                    .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                    .get(columnIndex);

            Comparator<Application> comparator = ApplicationComparators.getComparator(column.getData(), order.getDir());
            if (comparator == null) {
                return EMPTY_COMPARATOR;
            }
            return comparator;

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return EMPTY_COMPARATOR;
    }


}
