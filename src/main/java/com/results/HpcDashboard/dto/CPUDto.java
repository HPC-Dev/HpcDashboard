package com.results.HpcDashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CPUDto implements Serializable {
    @JsonProperty("cpu_sku")
    private String cpu_sku;

    @JsonProperty("cores")
    private int cores;

    public CPUDto(Object[] columns) {
        this.cpu_sku = (String) columns[0];
        this.cores = (int) columns[1];

    }
}
