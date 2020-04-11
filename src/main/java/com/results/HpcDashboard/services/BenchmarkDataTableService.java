package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.BenchmarkComparators;
import com.results.HpcDashboard.dto.BenchmarkDto;
import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.paging.*;

import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BenchmarkDataTableService implements DataTableService<BenchmarkDto> {

    @Autowired
    Util util;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Comparator<BenchmarkDto> EMPTY_COMPARATOR = (e1, e2) -> 0;

    public Page<BenchmarkDto> getData(PagingRequest pagingRequest)  {
        List<BenchmarkDto> benchmarks = util.getBenchmarks(entityManager);
        return getPage(benchmarks, pagingRequest);
    }

    public Page<BenchmarkDto> getPage(List<BenchmarkDto> benchmarks, PagingRequest pagingRequest) {
        List<BenchmarkDto> filtered = benchmarks.stream()
                .sorted(sortData(pagingRequest))
                .filter(filterData(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        long count = benchmarks.stream()
                .filter(filterData(pagingRequest))
                .count();

        Page<BenchmarkDto> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    public Predicate<BenchmarkDto> filterData(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return benchmark -> true;
        }

        String value = pagingRequest.getSearch()
                .getValue();

        return benchmark -> benchmark.getApp_name()
                .toLowerCase()
                .contains(value)
                || benchmark.getBm_name()
                .toLowerCase()
                .contains(value)
                || benchmark.getBm_full_name()
                .toLowerCase()
                .contains(value)
                || benchmark.getBm_metric()
                .toLowerCase()
                .contains(value)
                || benchmark.getBm_size()
                .toLowerCase()
                .contains(value)
                || benchmark.getBm_size_units()
                .toLowerCase()
                .contains(value)
                || benchmark.getBm_dur_units()
                .toLowerCase()
                .contains(value)
                || benchmark.getBm_units()
                .toLowerCase()
                .contains(value);

    }

    public Comparator<BenchmarkDto> sortData(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                    .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                    .get(columnIndex);

            Comparator<BenchmarkDto> comparator = BenchmarkComparators.getComparator(column.getData(), order.getDir());
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
