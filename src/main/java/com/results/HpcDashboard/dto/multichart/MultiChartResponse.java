package com.results.HpcDashboard.dto.multichart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MultiChartResponse {
    private String appName;
    private String metric;
    private List<String> labels;
    private List<Dataset> datasets;
}
