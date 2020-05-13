package com.results.HpcDashboard.dto.multichart;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class DatasetOld {
    private String cpuName;
    private List<Double> value;
}
