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
    private int bm_id;

    private String bm_name;

    private String bm_full_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_name")
    @Getter(AccessLevel.NONE)
    private Application app_name;

    private String bm_size;

    private String bm_size_units;

    private long bm_dur;

    private String bm_dur_units;

    private String bm_metric;

    private String  bm_units;

    private long est_runtime;


    public Benchmark(String bm_name, String bm_full_name, String bm_size, String bm_size_units, int bm_dur, String bm_dur_units, String bm_metric, String bm_units, int est_runtime) {

        this.bm_name = bm_name;
        this.bm_full_name = bm_full_name;
        this.bm_size = bm_size;
        this.bm_size_units = bm_size_units;
        this.bm_dur = bm_dur;
        this.bm_dur_units = bm_dur_units;
        this.bm_metric = bm_metric;
        this.bm_units = bm_units;
        this.est_runtime = est_runtime;
    }

    public Benchmark(String bm_name, String bm_full_name, String app_name, String bm_size, String bm_size_units, int bm_dur, String bm_dur_units, String bm_metric, String bm_units, int est_runtime) {

        this.app_name = new Application();
        this.bm_name = bm_name;
        this.bm_full_name = bm_full_name;
        this.app_name.setApp_name(app_name);
        this.bm_size = bm_size;
        this.bm_size_units = bm_size_units;
        this.bm_dur = bm_dur;
        this.bm_dur_units = bm_dur_units;
        this.bm_metric = bm_metric;
        this.bm_units = bm_units;
        this.est_runtime = est_runtime;
    }
}
