package com.results.HpcDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AverageResultId implements Serializable {

    String cpuSku;

    int nodes;

    String bmName;
}
