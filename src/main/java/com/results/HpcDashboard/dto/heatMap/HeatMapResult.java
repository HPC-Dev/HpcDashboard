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
    String perNode1;
    String perCore1;
    String perNode2;
    String perCore2;
    String perNode3;
    String perCore3;
}
