package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.paging.Page;
import com.results.HpcDashboard.paging.PagingRequest;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface DataTableService<T> {

    public Page<T> getData(PagingRequest pagingRequest);

    public Page<T> getPage(List<T> list, PagingRequest pagingRequest);

    public Predicate<T> filterData(PagingRequest pagingRequest);

    public Comparator<T> sortData(PagingRequest pagingRequest);

}
