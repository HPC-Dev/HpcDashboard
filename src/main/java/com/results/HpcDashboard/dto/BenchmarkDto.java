package com.results.HpcDashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkDto implements Serializable {
    @JsonProperty("app_name")
    private String app_name;

    @JsonProperty("bm_name")
    private String bm_name;

    @JsonProperty("bm_full_name")
    private String bm_full_name;

    @JsonProperty("bm_dur")
    private BigInteger bm_dur;

    @JsonProperty("bm_metric")
    private String bm_metric;

    @JsonProperty("bm_size")
    private String bm_size;

    @JsonProperty("bm_size_units")
    private String bm_size_units;

    @JsonProperty("bm_dur_units")
    private String bm_dur_units;

    @JsonProperty("bm_units")
    private String bm_units;

    @JsonProperty("est_runtime")
    private BigInteger est_runtime;


    public BenchmarkDto(Object[] columns) {
        this.app_name = (String) columns[0];
        this.bm_name = (String) columns[1];
        this.bm_full_name = (String) columns[2];
        this.bm_dur = (BigInteger) columns[3];
        this.bm_metric = (String) columns[4];
        this.bm_size = (String) columns[5];
        this.bm_size_units = (String) columns[6];
        this.bm_dur_units = (String) columns[7];
        this.bm_units = (String) columns[8];
        this.est_runtime = (BigInteger) columns[9];
    }
}
