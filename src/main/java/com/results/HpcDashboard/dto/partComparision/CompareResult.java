package com.results.HpcDashboard.dto.partComparision;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CompareResult {

    private String appName;

    private Set<String> bmName;

    private List<Map<String,String>> resultData;

}
