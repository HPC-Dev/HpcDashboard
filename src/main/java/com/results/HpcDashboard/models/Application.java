package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "applications")
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Application implements Serializable {
    @Id
    private String app_id;

    private String app_name;

    private String app_category;

    private String app_verion;

    private String precision_info;

    private String binary_info;

    private String comp_name;

    private String comp_ver;

    private String comp_flags;

    private String lib_name;

    private String lib_ver;

    private String lib_flags;

    private Boolean avx2_accel;

    private Boolean avx512_accel;

    private LocalDate lic_exp;

    private Boolean lib_fftw;

    private Boolean lib_dgemm;

    private Boolean lib_blas;

    private String app_bound;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "app_name", cascade = CascadeType.ALL)
    private Set<Benchmark> apps;


    public Application(String app_id, String app_name, String app_category, String app_verion, String precision_info, String binary_info, String comp_name, String comp_ver, String comp_flags, String lib_name, String lib_ver, String lib_flags, Boolean avx2_accel, Boolean avx512_accel, LocalDate lic_exp, Boolean lib_fftw, Boolean lib_dgemm, Boolean lib_blas, String app_bound, Set<Benchmark> apps) {
        this.app_id = app_id;
        this.app_name = app_name;
        this.app_category = app_category;
        this.app_verion = app_verion;
        this.precision_info = precision_info;
        this.binary_info = binary_info;
        this.comp_name = comp_name;
        this.comp_ver = comp_ver;
        this.comp_flags = comp_flags;
        this.lib_name = lib_name;
        this.lib_ver = lib_ver;
        this.lib_flags = lib_flags;
        this.avx2_accel = avx2_accel;
        this.avx512_accel = avx512_accel;
        this.lic_exp = lic_exp;
        this.lib_fftw = lib_fftw;
        this.lib_dgemm = lib_dgemm;
        this.lib_blas = lib_blas;
        this.app_bound = app_bound;
        this.apps=apps;
        this.apps.forEach(x -> x.setApp_name(this));
    }

}
