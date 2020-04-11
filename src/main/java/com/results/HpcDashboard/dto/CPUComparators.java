package com.results.HpcDashboard.dto;

import com.results.HpcDashboard.models.CPU;
import com.results.HpcDashboard.paging.Direction;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class CPUComparators {

    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    static class Key {
        String name;
        Direction dir;
    }

    static Map<Key, Comparator<CPU>> map = new HashMap<>();

    static {
        map.put(new Key("cores", Direction.asc), Comparator.comparing(CPU::getCores));
        map.put(new Key("cores", Direction.desc), Comparator.comparing(CPU::getCores)
                .reversed());

        map.put(new Key("all_core_freq", Direction.asc), Comparator.comparing(CPU::getAll_core_freq));
        map.put(new Key("all_core_freq", Direction.desc), Comparator.comparing(CPU::getAll_core_freq)
                .reversed());

        map.put(new Key("cpu_manufacturer", Direction.asc), Comparator.comparing(CPU::getCpu_manufacturer));
        map.put(new Key("cpu_manufacturer", Direction.desc), Comparator.comparing(CPU::getCpu_manufacturer)
                .reversed());

        map.put(new Key("cpu_generation", Direction.asc), Comparator.comparing(CPU::getCpu_generation));
        map.put(new Key("cpu_generation", Direction.desc), Comparator.comparing(CPU::getCpu_generation)
                .reversed());

        map.put(new Key("cpu_sku", Direction.asc), Comparator.comparing(CPU::getCpu_sku));
        map.put(new Key("cpu_sku", Direction.desc), Comparator.comparing(CPU::getCpu_sku)
                .reversed());

        map.put(new Key("tdp", Direction.asc), Comparator.comparing(CPU::getTdp));
        map.put(new Key("tdp", Direction.desc), Comparator.comparing(CPU::getTdp)
                .reversed());

        map.put(new Key("base_freq", Direction.asc), Comparator.comparing(CPU::getBase_freq));
        map.put(new Key("base_freq", Direction.desc), Comparator.comparing(CPU::getBase_freq)
                .reversed());

        map.put(new Key("peak_freq", Direction.asc), Comparator.comparing(CPU::getPeak_freq));
        map.put(new Key("peak_freq", Direction.desc), Comparator.comparing(CPU::getPeak_freq)
                .reversed());

        map.put(new Key("l3_cache", Direction.asc), Comparator.comparing(CPU::getL3_cache));
        map.put(new Key("l3_cache", Direction.desc), Comparator.comparing(CPU::getL3_cache)
                .reversed());

        map.put(new Key("ddr_channels", Direction.asc), Comparator.comparing(CPU::getDdr_channels));
        map.put(new Key("ddr_channels", Direction.desc), Comparator.comparing(CPU::getDdr_channels)
                .reversed());

        map.put(new Key("max_ddr_freq", Direction.asc), Comparator.comparing(CPU::getMax_ddr_freq));
        map.put(new Key("max_ddr_freq", Direction.desc), Comparator.comparing(CPU::getMax_ddr_freq)
                .reversed());
    }

    public static Comparator<CPU> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private CPUComparators() {
    }
}
