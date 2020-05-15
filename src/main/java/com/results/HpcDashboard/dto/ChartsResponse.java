package com.results.HpcDashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class ChartsResponse {
    private String appCPUName;
    private String metric;
    private List<String> labels;
    private List<String> labelsTable;
    private List<Double> dataset;
    private List<Map<String,String>> tableDataset;
    private String comment;
}
