package com.results.HpcDashboard.dto.scatter;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Data
public class ScatterTableResponse {

    private String appName;
    private Set<String> nodes;
    private List<Map<String,String>> resultData;
}
