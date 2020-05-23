package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "benchmarks")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Benchmark implements Serializable {

    @Id
    private String bmName;

    private String bmFullName;

    private String appName;

    private String bmSize;

    private String bmSizeUnits;

    private long bmDur;

    private String bmDurUnits;

    private String bmMetric;

    private String  bmUnits;

    private long estRuntime;

     public Benchmark(String bmName, String appName, String bmFullName, String bmSize, String bmSizeUnits, long bmDur, String bmDurUnits, String bmMetric, String bmUnits, long estRuntime) {
        this.appName = appName;
        this.bmName = bmName;
        this.bmFullName = bmFullName;
        this.bmSize = bmSize;
        this.bmSizeUnits = bmSizeUnits;
        this.bmDur = bmDur;
        this.bmDurUnits = bmDurUnits;
        this.bmMetric = bmMetric;
        this.bmUnits = bmUnits;
        this.estRuntime = estRuntime;
    }
}
