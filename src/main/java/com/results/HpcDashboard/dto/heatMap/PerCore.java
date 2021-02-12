package com.results.HpcDashboard.dto.heatMap;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class PerCore {

    int cores;
    double result;
    String comment;
}
