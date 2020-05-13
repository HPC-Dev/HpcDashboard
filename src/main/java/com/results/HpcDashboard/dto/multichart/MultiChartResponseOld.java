package com.results.HpcDashboard.dto.multichart;

import lombok.Data;

import java.util.List;

@Data
public class MultiChartResponseOld {
    private String appName;
    private String metric;
    private List<String> labels;
    private List<DatasetOld> datasets;
    private String comment;
}
