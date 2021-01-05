package com.results.HpcDashboard.dto.partComparison;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class TwoPartChartDataset {
    private String cpuName;
    private List<Double> value;
}
