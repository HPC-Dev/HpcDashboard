package com.results.HpcDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HeatMapId implements Serializable {

    int nodes;

    String bmName;

    String cpuSku;

    String runType;
}
