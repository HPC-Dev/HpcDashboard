package com.results.HpcDashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobDto implements Serializable {
    @JsonProperty("bmName")
    private String bmName;

    @JsonProperty("cpu")
    private String cpu;

    @JsonProperty("nodes")
    private Integer nodes;

    public JobDto(Object[] column) {
        this.bmName = (String) column[0];
        this.cpu = (String) column[1];
        this.nodes = (int) column[2];
    }
}
