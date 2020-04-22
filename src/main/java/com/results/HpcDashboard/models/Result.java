package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @CsvBindByPosition(position = 0)
    @Id
    private String jobId;

    @CsvBindByPosition(position = 1)
    private String appName;

    @CsvBindByPosition(position = 2)
    private String bmName;

    @CsvBindByPosition(position = 6)
    private int nodes;

    @CsvBindByPosition(position = 3)
    private int cores;

    @CsvBindByPosition(position = 5)
    private String nodeName;

    @CsvBindByPosition(position = 7)
    private double result;

    @CsvBindByPosition(position = 4)
    private String cpu;

}
