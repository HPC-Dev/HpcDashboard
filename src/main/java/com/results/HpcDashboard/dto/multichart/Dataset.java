package com.results.HpcDashboard.dto.multichart;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Dataset {
    private String cpuName;
    private List<Double> value;
}
