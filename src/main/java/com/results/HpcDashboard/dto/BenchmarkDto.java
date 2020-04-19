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
    @JsonProperty("appName")
    private String appName;

    @JsonProperty("bmName")
    private String bmName;

    @JsonProperty("bmFullName")
    private String bmFullName;

    @JsonProperty("bmDur")
    private BigInteger bmDur;

    @JsonProperty("bmMetric")
    private String bmMetric;

    @JsonProperty("bmSize")
    private String bmSize;

    @JsonProperty("bmSizeUnits")
    private String bmSizeUnits;

    @JsonProperty("bmDurUnits")
    private String bmDurUnits;

    @JsonProperty("bmUnits")
    private String bmUnits;

    @JsonProperty("estRuntime")
    private BigInteger estRuntime;


    public BenchmarkDto(Object[] columns) {
        this.appName = (String) columns[0];
        this.bmName = (String) columns[1];
        this.bmFullName = (String) columns[2];
        this.bmDur = (BigInteger) columns[3];
        this.bmMetric = (String) columns[4];
        this.bmSize = (String) columns[5];
        this.bmSizeUnits = (String) columns[6];
        this.bmDurUnits = (String) columns[7];
        this.bmUnits = (String) columns[8];
        this.estRuntime = (BigInteger) columns[9];
    }



}
