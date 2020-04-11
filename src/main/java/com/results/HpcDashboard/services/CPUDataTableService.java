package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.CPUComparators;
import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.paging.*;

import com.results.HpcDashboard.repo.CPURepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CPUDataTableService implements DataTableService<CPU> {

    @Autowired
    CPUService cpuService;
    private static final Comparator<CPU> EMPTY_COMPARATOR = (e1, e2) -> 0;

    public Page<CPU> getData(PagingRequest pagingRequest)  {
        List<CPU> cpus = cpuService.getAllCPUs();
        return getPage(cpus, pagingRequest);
    }

    public Page<CPU> getPage(List<CPU> cpus, PagingRequest pagingRequest) {
        List<CPU> filtered = cpus.stream()
                .sorted(sortData(pagingRequest))
                .filter(filterData(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        long count = cpus.stream()
                .filter(filterData(pagingRequest))
                .count();

        Page<CPU> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    public Predicate<CPU> filterData(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return cpu -> true;
        }

        String value = pagingRequest.getSearch()
                .getValue();

        return cpu -> cpu.getCpu_generation()
                .toLowerCase()
                .contains(value)
                || cpu.getCpu_manufacturer()
                .toLowerCase()
                .contains(value)
                || cpu.getCpu_sku()
                .toLowerCase()
                .contains(value);
    }

    public Comparator<CPU> sortData(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                    .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                    .get(columnIndex);

            Comparator<CPU> comparator = CPUComparators.getComparator(column.getData(), order.getDir());
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