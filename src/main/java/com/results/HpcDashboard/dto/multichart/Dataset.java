package com.results.HpcDashboard.dto.multichart;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Dataset {
    private String bmName;
    private List<Double> value;
}
