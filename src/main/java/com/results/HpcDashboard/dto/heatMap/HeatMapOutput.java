package com.results.HpcDashboard.dto.heatMap;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class HeatMapOutput {

    List<String> columns;

    List<HeatMapResult> heatMapResults;


}
