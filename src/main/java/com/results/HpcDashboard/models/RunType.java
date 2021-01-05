package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.results.HpcDashboard.dto.RunTypeId;
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
@IdClass(RunTypeId.class)
@Table(name = "run_types")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class RunType {

    String mode;

    String ddr4_freq;

    int nps;

    String smt;

    String os;

    String cTDP;

    String boost;

    @Id
    String runType;

    @Id
    String cpu;

    @Id
    String app_name;

    String apbdis;

    String soc_pstate;

    String x2APIC;

    String determinism;

    String preferred_io;

    String iommu;

    String other;

}

