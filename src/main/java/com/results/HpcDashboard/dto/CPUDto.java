package com.results.HpcDashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
