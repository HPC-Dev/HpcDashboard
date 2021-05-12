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
    double per_Core_Uplift;
    double per_Dollar_Uplift;
    double per_Watt_Uplift;
    Map<String, Double> bmUplift;
    Map<String, Double> perCoreBmUplift;
    Map<String, Double> perDollarBmUplift;
    Map<String, Double> perWattBmUplift;
}
