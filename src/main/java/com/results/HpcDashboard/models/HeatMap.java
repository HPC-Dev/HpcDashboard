package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.results.HpcDashboard.dto.HeatMapId;
import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@IdClass(HeatMapId.class)
@Table(name = "heat_map")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class HeatMap {

    String category;

    String isv;

    String appName;

    @Id
    int nodes;

    @Id
    String bmName;

    @Id
    String cpuSku;

    double avgResult;

    @Id
    String runType;

}

