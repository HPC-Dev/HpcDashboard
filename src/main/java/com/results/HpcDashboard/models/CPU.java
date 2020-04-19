package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "cpu_info")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
public class CPU implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String cpuManufacturer;

    String cpuGeneration;

    String cpuSku;

    String tdp;

    int cores;

    String baseFreq;

    String allCoreFreq;

    String peakFreq;

    int l3Cache;

    int ddrChannels;

    String maxDdrFreq;

}
