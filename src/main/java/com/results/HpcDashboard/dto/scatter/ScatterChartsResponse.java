package com.results.HpcDashboard.dto.scatter;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Data
public class ScatterChartsResponse {
    private String appCPUName;
    private Set<Integer> nodes;
    private Set<String> labels;
    private List<Map<Integer,Double>> dataset;
    private String comment;
}
