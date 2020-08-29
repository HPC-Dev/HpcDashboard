package com.results.HpcDashboard.dto.heatMap;

import lombok.*;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class ISV {
    String isv;
    Set<App> app;
}
