package com.results.HpcDashboard.dto;

import com.results.HpcDashboard.paging.Direction;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class BenchmarkComparators {

    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    static class Key {
        String name;
        Direction dir;
    }

    static Map<Key, Comparator<BenchmarkDto>> map = new HashMap<>();

    static {
        map.put(new Key("bm_id", Direction.asc), Comparator.comparing(BenchmarkDto::getApp_name));
        map.put(new Key("bm_id", Direction.desc), Comparator.comparing(BenchmarkDto::getApp_name)
                .reversed());

        map.put(new Key("bm_name", Direction.asc), Comparator.comparing(BenchmarkDto::getBm_name));
        map.put(new Key("bm_name", Direction.desc), Comparator.comparing(BenchmarkDto::getBm_name)
                .reversed());

        map.put(new Key("bm_full_name", Direction.asc), Comparator.comparing(BenchmarkDto::getBm_full_name));
        map.put(new Key("bm_full_name", Direction.desc), Comparator.comparing(BenchmarkDto::getBm_full_name)
                .reversed());

        map.put(new Key("app_name", Direction.asc), Comparator.comparing(BenchmarkDto::getApp_name));
        map.put(new Key("app_name", Direction.desc), Comparator.comparing(BenchmarkDto::getApp_name)
                .reversed());

        map.put(new Key("bm_size", Direction.asc), Comparator.comparing(BenchmarkDto::getBm_size));
        map.put(new Key("bm_size", Direction.desc), Comparator.comparing(BenchmarkDto::getBm_size)
                .reversed());

        map.put(new Key("bm_size_units", Direction.asc), Comparator.comparing(BenchmarkDto::getBm_size_units));
        map.put(new Key("bm_size_units", Direction.desc), Comparator.comparing(BenchmarkDto::getBm_size_units)
                .reversed());

        map.put(new Key("bm_dur", Direction.asc), Comparator.comparing(BenchmarkDto::getBm_dur));
        map.put(new Key("bm_dur", Direction.desc), Comparator.comparing(BenchmarkDto::getBm_dur)
                .reversed());

        map.put(new Key("bm_dur_units", Direction.asc), Comparator.comparing(BenchmarkDto::getBm_dur_units));
        map.put(new Key("bm_dur_units", Direction.desc), Comparator.comparing(BenchmarkDto::getBm_dur_units)
                .reversed());

        map.put(new Key("est_runtime", Direction.asc), Comparator.comparing(BenchmarkDto::getEst_runtime));
        map.put(new Key("est_runtime", Direction.desc), Comparator.comparing(BenchmarkDto::getEst_runtime)
                .reversed());

        map.put(new Key("bm_units", Direction.asc), Comparator.comparing(BenchmarkDto::getBm_units));
        map.put(new Key("bm_units", Direction.desc), Comparator.comparing(BenchmarkDto::getBm_units)
                .reversed());

        map.put(new Key("bm_metric", Direction.asc), Comparator.comparing(BenchmarkDto::getBm_metric));
        map.put(new Key("bm_metric", Direction.desc), Comparator.comparing(BenchmarkDto::getBm_metric)
                .reversed());
    }

    public static Comparator<BenchmarkDto> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private BenchmarkComparators() {
    }
}
