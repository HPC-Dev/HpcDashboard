package com.results.HpcDashboard.dto;

import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.paging.Direction;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class ApplicationComparators {

    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    static class Key {
        String name;
        Direction dir;
    }


    static Map<Key, Comparator<Application>> map = new HashMap<>();

    static {
        map.put(new Key("app_id", Direction.asc), Comparator.comparing(Application::getApp_id));
        map.put(new Key("app_id", Direction.desc), Comparator.comparing(Application::getApp_id)
                .reversed());

        map.put(new Key("app_name", Direction.asc), Comparator.comparing(Application::getApp_name));
        map.put(new Key("app_name", Direction.desc), Comparator.comparing(Application::getApp_name)
                .reversed());

        map.put(new Key("app_verion", Direction.asc), Comparator.comparing(Application::getApp_verion));
        map.put(new Key("app_verion", Direction.desc), Comparator.comparing(Application::getApp_verion)
                .reversed());

        map.put(new Key("precision_info", Direction.asc), Comparator.comparing(Application::getPrecision_info));
        map.put(new Key("precision_info", Direction.desc), Comparator.comparing(Application::getPrecision_info)
                .reversed());

        map.put(new Key("binary_info", Direction.asc), Comparator.comparing(Application::getBinary_info));
        map.put(new Key("binary_info", Direction.desc), Comparator.comparing(Application::getBinary_info)
                .reversed());

        map.put(new Key("comp_name", Direction.asc), Comparator.comparing(Application::getComp_name));
        map.put(new Key("comp_name", Direction.desc), Comparator.comparing(Application::getComp_name)
                .reversed());

        map.put(new Key("comp_flags", Direction.asc), Comparator.comparing(Application::getComp_flags));
        map.put(new Key("comp_flags", Direction.desc), Comparator.comparing(Application::getComp_flags)
                .reversed());

        map.put(new Key("lib_name", Direction.asc), Comparator.comparing(Application::getLib_name));
        map.put(new Key("lib_name", Direction.desc), Comparator.comparing(Application::getLib_name)
                .reversed());

        map.put(new Key("lib_ver", Direction.asc), Comparator.comparing(Application::getLib_ver));
        map.put(new Key("lib_ver", Direction.desc), Comparator.comparing(Application::getLib_ver)
                .reversed());

        map.put(new Key("lib_flags", Direction.asc), Comparator.comparing(Application::getLib_flags));
        map.put(new Key("lib_flags", Direction.desc), Comparator.comparing(Application::getLib_flags)
                .reversed());

        map.put(new Key("avx2_accel", Direction.asc), Comparator.comparing(Application::getAvx2_accel));
        map.put(new Key("avx2_accel", Direction.desc), Comparator.comparing(Application::getAvx2_accel)
                .reversed());

        map.put(new Key("avx512_accel", Direction.asc), Comparator.comparing(Application::getAvx512_accel));
        map.put(new Key("avx512_accel", Direction.desc), Comparator.comparing(Application::getAvx512_accel)
                .reversed());


        map.put(new Key("lic_exp", Direction.asc), Comparator.comparing(Application::getLic_exp));
        map.put(new Key("lic_exp", Direction.desc), Comparator.comparing(Application::getLic_exp)
                .reversed());
        map.put(new Key("lib_blas", Direction.asc), Comparator.comparing(Application::getLib_blas));
        map.put(new Key("lib_blas", Direction.desc), Comparator.comparing(Application::getLib_blas)
                .reversed());

        map.put(new Key("lib_dgemm", Direction.asc), Comparator.comparing(Application::getLib_dgemm));
        map.put(new Key("lib_dgemm", Direction.desc), Comparator.comparing(Application::getLib_dgemm)
                .reversed());

        map.put(new Key("lib_fftw", Direction.asc), Comparator.comparing(Application::getLib_fftw));
        map.put(new Key("lib_fftw", Direction.desc), Comparator.comparing(Application::getLib_fftw)
                .reversed());

        map.put(new Key("app_bound", Direction.asc), Comparator.comparing(Application::getApp_bound));
        map.put(new Key("app_bound", Direction.desc), Comparator.comparing(Application::getApp_bound)
                .reversed());
    }


    public static Comparator<Application> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private ApplicationComparators() {
    }
}
