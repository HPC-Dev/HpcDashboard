package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
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

    @JsonAlias({"Job Id","Job_Id", "jobId"})
    @CsvBindByPosition(position = 0)
    @Id
    private String jobId;

    @JsonAlias({"App Name", "appName","App_Name"})
    @CsvBindByPosition(position = 1)
    private String appName;

    @JsonAlias({"Benchmark", "bmName"})
    @CsvBindByPosition(position = 2)
    private String bmName;

    @JsonAlias({"Nodes","nodes"})
    @CsvBindByPosition(position = 6)
    private int nodes;

    @JsonAlias({"Cores", "cores"})
    @CsvBindByPosition(position = 3)
    private int cores;

    @JsonAlias({"Node Name", "Node_Name", "nodeName"})
    @CsvBindByPosition(position = 5)
    private String nodeName;

    @JsonAlias({"Result", "result"})
    @CsvBindByPosition(position = 7)
    private double result;

    @JsonAlias({"CPU", "cpu"})
    @CsvBindByPosition(position = 4)
    private String cpu;

}
