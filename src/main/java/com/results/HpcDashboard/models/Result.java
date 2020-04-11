package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "results")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
public class Result implements Serializable {
    @Id
    private String job_id;

    private String app_name;

    private String bm_name;

    private int nodes;

    private int cores;

    private String node_name;

    private BigDecimal result;

    private String cpu;

}
