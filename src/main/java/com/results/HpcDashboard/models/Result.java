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
    private String jobId;

    private String appName;

    private String bmName;

    private int nodes;

    private int cores;

    private String nodeName;

    private double result;

    private String cpu;

}
