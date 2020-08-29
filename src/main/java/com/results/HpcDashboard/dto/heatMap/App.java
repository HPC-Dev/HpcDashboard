package com.results.HpcDashboard.dto.heatMap;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class App {

    String application;
    double uplift;
    Map<String, Double> bmUplift;
}
