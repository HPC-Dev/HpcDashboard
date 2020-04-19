package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "benchmarks")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Benchmark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bmId;

    private String bmName;

    private String bmFullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appName")
    @Getter(AccessLevel.NONE)
    private Application appName;

    private String bmSize;

    private String bmSizeUnits;

    private long bmDur;

    private String bmDurUnits;

    private String bmMetric;

    private String  bmUnits;

    private long estRuntime;

     public Benchmark(String bmName, String bmFullName, String bmSize, String bmSizeUnits, long bmDur, String bmDurUnits, String bmMetric, String bmUnits, long estRuntime) {
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


    public Benchmark(String bmName, String bmFullName, String appName, String bmSize, String bmSizeUnits, long bmDur, String bmDurUnits, String bmMetric, String bmUnits, long estRuntime) {

        this.appName = new Application();
        this.bmName = bmName;
        this.bmFullName = bmFullName;
        this.appName.setAppName(appName);
        this.bmSize = bmSize;
        this.bmSizeUnits = bmSizeUnits;
        this.bmDur = bmDur;
        this.bmDurUnits = bmDurUnits;
        this.bmMetric = bmMetric;
        this.bmUnits = bmUnits;
        this.estRuntime = estRuntime;
    }

}
