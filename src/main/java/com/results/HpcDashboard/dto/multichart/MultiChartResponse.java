package com.results.HpcDashboard.dto.multichart;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class MultiChartResponse {
    private String appName;
    private Set<String> cpus;
    private List<Dataset> dataset;
}
