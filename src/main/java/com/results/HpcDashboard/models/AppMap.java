package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "app_map")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
public class AppMap implements Serializable {

    @Id
    String appName;

    String metric;

    String status;
}
