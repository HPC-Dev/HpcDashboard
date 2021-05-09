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
@Table(name = "processor_info")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
public class Processor implements Serializable {

    String cpuGeneration;

    @Id
    String cpuSku;

    int cores;

    double price;
}
