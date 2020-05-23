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
    private String appId;

    private String appName;

    private String appCategory;

    private String appVerion;

    private String precisionInfo;

    private String binaryInfo;

    private String compName;

    private String compVer;

    private String compFlags;

    private String libName;

    private String libVer;

    private String libFlags;

    private Boolean avx2Accel;

    private Boolean avx512Accel;

    private LocalDate licExp;

    private Boolean libFftw;

    private Boolean libDgemm;

    private Boolean libBlas;

    private String appBound;


    public Application(String appId, String appName, String appCategory, String appVerion, String precisionInfo, String binaryInfo, String compName, String compVer, String compFlags, String libName, String libVer, String libFlags, Boolean avx2Accel, Boolean avx512Accel, LocalDate licExp, Boolean libFftw, Boolean libDgemm, Boolean libBlas, String appBound) {
        this.appId = appId;
        this.appName = appName;
        this.appCategory = appCategory;
        this.appVerion = appVerion;
        this.precisionInfo = precisionInfo;
        this.binaryInfo = binaryInfo;
        this.compName = compName;
        this.compVer = compVer;
        this.compFlags = compFlags;
        this.libName = libName;
        this.libVer = libVer;
        this.libFlags = libFlags;
        this.avx2Accel = avx2Accel;
        this.avx512Accel = avx512Accel;
        this.licExp = licExp;
        this.libFftw = libFftw;
        this.libDgemm = libDgemm;
        this.libBlas = libBlas;
        this.appBound = appBound;
    }
}
