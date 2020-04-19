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

        map.put(new Key("bmName", Direction.asc), Comparator.comparing(BenchmarkDto::getBmName));
        map.put(new Key("bmName", Direction.desc), Comparator.comparing(BenchmarkDto::getBmName)
                .reversed());

        map.put(new Key("bmFullName", Direction.asc), Comparator.comparing(BenchmarkDto::getBmFullName));
        map.put(new Key("bmFullName", Direction.desc), Comparator.comparing(BenchmarkDto::getBmFullName)
                .reversed());

        map.put(new Key("appName", Direction.asc), Comparator.comparing(BenchmarkDto::getAppName));
        map.put(new Key("appName", Direction.desc), Comparator.comparing(BenchmarkDto::getAppName)
                .reversed());

        map.put(new Key("bmSize", Direction.asc), Comparator.comparing(BenchmarkDto::getBmSize));
        map.put(new Key("bmSize", Direction.desc), Comparator.comparing(BenchmarkDto::getBmSize)
                .reversed());

        map.put(new Key("bmSizeUnits", Direction.asc), Comparator.comparing(BenchmarkDto::getBmSizeUnits));
        map.put(new Key("bmSizeUnits", Direction.desc), Comparator.comparing(BenchmarkDto::getBmSizeUnits)
                .reversed());

        map.put(new Key("bmDur", Direction.asc), Comparator.comparing(BenchmarkDto::getBmDur));
        map.put(new Key("bmDur", Direction.desc), Comparator.comparing(BenchmarkDto::getBmDur)
                .reversed());

        map.put(new Key("bmDurUnits", Direction.asc), Comparator.comparing(BenchmarkDto::getBmDurUnits));
        map.put(new Key("bmDurUnits", Direction.desc), Comparator.comparing(BenchmarkDto::getBmDurUnits)
                .reversed());

        map.put(new Key("estRuntime", Direction.asc), Comparator.comparing(BenchmarkDto::getEstRuntime));
        map.put(new Key("estRuntime", Direction.desc), Comparator.comparing(BenchmarkDto::getEstRuntime)
                .reversed());

        map.put(new Key("bmUnits", Direction.asc), Comparator.comparing(BenchmarkDto::getBmUnits));
        map.put(new Key("bmUnits", Direction.desc), Comparator.comparing(BenchmarkDto::getBmUnits)
                .reversed());

        map.put(new Key("bmMetric", Direction.asc), Comparator.comparing(BenchmarkDto::getBmMetric));
        map.put(new Key("bmMetric", Direction.desc), Comparator.comparing(BenchmarkDto::getBmMetric)
                .reversed());
    }

    public static Comparator<BenchmarkDto> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private BenchmarkComparators() {
    }
}
