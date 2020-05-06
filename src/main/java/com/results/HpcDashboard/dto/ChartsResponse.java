package com.results.HpcDashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ChartsResponse {
    private String appCPUName;
    private String metric;
    private List<String> labels;
    private List<Double> dataset;
    private String comment;
}
