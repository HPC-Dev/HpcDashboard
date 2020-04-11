package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.ResultComparators;
import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.paging.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ResultDashboardService implements DataTableService<Result> {

    @Autowired
    ResultService resultService;
    private static final Comparator<Result> EMPTY_COMPARATOR = (e1, e2) -> 0;

    public Page<Result> getData(PagingRequest pagingRequest)  {
            List<Result> results = resultService.getAllResults();
            return getPage(results, pagingRequest);
}

    public Page<Result> getPage(List<Result> results, PagingRequest pagingRequest) {
        List<Result> filtered = results.stream()
                .sorted(sortData(pagingRequest))
                .filter(filterData(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        long count = results.stream()
                .filter(filterData(pagingRequest))
                .count();

        Page<Result> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    public Predicate<Result> filterData(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return result -> true;
        }

        String value = pagingRequest.getSearch()
                .getValue();

        return result -> result.getApp_name()
                .toLowerCase()
                .contains(value)
                || result.getBm_name()
                .toLowerCase()
                .contains(value)
                || result.getNode_name()
                .toLowerCase()
                .contains(value)
                || result.getCpu()
                .toLowerCase()
                .contains(value)
                || result.getCpu()
                .toUpperCase()
                .contains(value)
                || result.getJob_id()
                .contains(value);
    }

    public Comparator<Result> sortData(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                    .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                    .get(columnIndex);

            Comparator<Result> comparator = ResultComparators.getComparator(column.getData(), order.getDir());
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


