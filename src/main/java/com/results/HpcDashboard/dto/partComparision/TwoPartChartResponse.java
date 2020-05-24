package com.results.HpcDashboard.dto.partComparision;

import lombok.Data;

import java.util.List;

@Data
public class TwoPartChartResponse {
    private String appName;
    private String metric;
    private List<String> labels;
    private List<TwoPartChartDataset> datasets;
    private String comment;
}
