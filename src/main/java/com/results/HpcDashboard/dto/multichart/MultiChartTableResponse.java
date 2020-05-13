package com.results.HpcDashboard.dto.multichart;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Data
public class MultiChartTableResponse {

    private String appName;
    private List<String> label;
    private List<Map<String,String>> resultData;
}
