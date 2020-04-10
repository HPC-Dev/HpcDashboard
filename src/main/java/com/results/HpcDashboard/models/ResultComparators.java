package com.results.HpcDashboard.models;

import java.util.*;

import com.results.HpcDashboard.paging.Direction;
import lombok.*;
public final class ResultComparators {

    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    static class Key {
        String name;
        Direction dir;
    }

    static Map<Key, Comparator<Result>> map = new HashMap<>();

    static {
        map.put(new Key("app_name", Direction.asc), Comparator.comparing(Result::getApp_name));
        map.put(new Key("app_name", Direction.desc), Comparator.comparing(Result::getApp_name)
                .reversed());

        map.put(new Key("bm_name", Direction.asc), Comparator.comparing(Result::getBm_name));
        map.put(new Key("bm_name", Direction.desc), Comparator.comparing(Result::getBm_name)
                .reversed());

        map.put(new Key("node_name", Direction.asc), Comparator.comparing(Result::getNode_name));
        map.put(new Key("node_name", Direction.desc), Comparator.comparing(Result::getNode_name)
                .reversed());

        map.put(new Key("cpu", Direction.asc), Comparator.comparing(Result::getCpu));
        map.put(new Key("cpu", Direction.desc), Comparator.comparing(Result::getCpu)
                .reversed());

        map.put(new Key("cores", Direction.asc), Comparator.comparing(Result::getCores));
        map.put(new Key("cores", Direction.desc), Comparator.comparing(Result::getCores)
                .reversed());

        map.put(new Key("job_id", Direction.asc), Comparator.comparing(Result::getJob_id));
        map.put(new Key("job_id", Direction.desc), Comparator.comparing(Result::getJob_id)
                .reversed());

        map.put(new Key("nodes", Direction.asc), Comparator.comparing(Result::getNodes));
        map.put(new Key("nodes", Direction.desc), Comparator.comparing(Result::getNodes)
                .reversed());

        map.put(new Key("result", Direction.asc), Comparator.comparing(Result::getResult));
        map.put(new Key("result", Direction.desc), Comparator.comparing(Result::getResult)
                .reversed());
    }

    public static Comparator<Result> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private ResultComparators() {
    }
}
