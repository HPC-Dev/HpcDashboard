package com.results.HpcDashboard.dto.heatMap;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Category {
    String Category;
    double uplift;
    double per_Core_Uplift;
    Set<ISV> isvList;
}
