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

    String cpu_manufacturer;

    String cpu_generation;

    String cpu_sku;

    int tdp;

    int cores;

    double base_freq;

    double all_core_freq;

    double peak_freq;

    int l3_cache;

    int ddr_channels;

    int max_ddr_freq;

}
