package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.results.HpcDashboard.dto.AverageResultId;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "average_result")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
@IdClass(AverageResultId.class)
@Entity
public class AverageResult implements Serializable {

    @Id
    String cpu_sku;

    @Id
    int nodes;

    @Id
    String bm_name;

    int cores;

    String app_name;

    double avg_result;

    double variance;

}
