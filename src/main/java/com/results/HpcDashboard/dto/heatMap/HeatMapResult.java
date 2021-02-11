package com.results.HpcDashboard.dto.heatMap;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class HeatMapResult {

    String Category;
    String ISV;
    String Application;
    String Benchmark;
    String perNode;
    String perCore;
}
