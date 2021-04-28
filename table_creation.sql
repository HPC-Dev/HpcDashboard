CREATE TABLE `app_category` (
  `bm_name` varchar(255) NOT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `isv` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bm_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `applications` (
  `app_id` varchar(255) NOT NULL,
  `app_bound` varchar(255) DEFAULT NULL,
  `app_category` varchar(255) DEFAULT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `app_verion` varchar(255) DEFAULT NULL,
  `avx2accel` bit(1) DEFAULT NULL,
  `avx512accel` bit(1) DEFAULT NULL,
  `binary_info` varchar(255) DEFAULT NULL,
  `comp_flags` varchar(255) DEFAULT NULL,
  `comp_name` varchar(255) DEFAULT NULL,
  `comp_ver` varchar(255) DEFAULT NULL,
  `lib_blas` bit(1) DEFAULT NULL,
  `lib_dgemm` bit(1) DEFAULT NULL,
  `lib_fftw` bit(1) DEFAULT NULL,
  `lib_flags` varchar(255) DEFAULT NULL,
  `lib_name` varchar(255) DEFAULT NULL,
  `lib_ver` varchar(255) DEFAULT NULL,
  `lic_exp` date DEFAULT NULL,
  `precision_info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `average_result` (
  `bm_name` varchar(255) NOT NULL,
  `cpu_sku` varchar(255) NOT NULL,
  `nodes` int NOT NULL,
  `run_type` varchar(255) NOT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `avg_result` double NOT NULL,
  `coefficient_of_variation` double NOT NULL,
  `cores` int NOT NULL,
  `per_core_perf` double NOT NULL,
  `run_count` int NOT NULL,
  PRIMARY KEY (`bm_name`,`cpu_sku`,`nodes`,`run_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `benchmarks` (
  `bm_name` varchar(255) NOT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `bm_dur` bigint NOT NULL,
  `bm_dur_units` varchar(255) DEFAULT NULL,
  `bm_full_name` varchar(255) DEFAULT NULL,
  `bm_metric` varchar(255) DEFAULT NULL,
  `bm_size` varchar(255) DEFAULT NULL,
  `bm_size_units` varchar(255) DEFAULT NULL,
  `bm_units` varchar(255) DEFAULT NULL,
  `est_runtime` bigint NOT NULL,
  PRIMARY KEY (`bm_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cpu_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `all_core_freq` varchar(255) DEFAULT NULL,
  `base_freq` varchar(255) DEFAULT NULL,
  `cores` int NOT NULL,
  `cpu_generation` varchar(255) DEFAULT NULL,
  `cpu_manufacturer` varchar(255) DEFAULT NULL,
  `cpu_sku` varchar(255) DEFAULT NULL,
  `ddr_channels` int NOT NULL,
  `l3cache` varchar(255) DEFAULT NULL,
  `max_ddr_freq` varchar(255) DEFAULT NULL,
  `peak_freq` varchar(255) DEFAULT NULL,
  `tdp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `heat_map` (
  `bm_name` varchar(255) NOT NULL,
  `cpu_sku` varchar(255) NOT NULL,
  `nodes` int NOT NULL,
  `run_type` varchar(255) NOT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `avg_result` double NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `cores` int NOT NULL,
  `isv` varchar(255) DEFAULT NULL,
  `per_core_perf` double NOT NULL,
  `run_count` int NOT NULL,
  PRIMARY KEY (`bm_name`,`cpu_sku`,`nodes`,`run_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `results` (
  `job_id` varchar(255) NOT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `bios_ver` varchar(255) DEFAULT NULL,
  `bm_name` varchar(255) DEFAULT NULL,
  `cluster` varchar(255) DEFAULT NULL,
  `cores` int NOT NULL,
  `cpu` varchar(255) DEFAULT NULL,
  `cpu_gen` varchar(255) DEFAULT NULL,
  `node_name` varchar(255) DEFAULT NULL,
  `nodes` int NOT NULL,
  `os` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `result` double NOT NULL,
  `run_type` varchar(255) DEFAULT NULL,
  `time_stamp` datetime DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `role` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `run_types` (
  `app_name` varchar(255) NOT NULL,
  `cpu` varchar(255) NOT NULL,
  `run_type` varchar(255) NOT NULL,
  `apbdis` varchar(255) DEFAULT NULL,
  `boost` varchar(255) DEFAULT NULL,
  `ctdp` varchar(255) DEFAULT NULL,
  `ddr4_freq` varchar(255) DEFAULT NULL,
  `determinism` varchar(255) DEFAULT NULL,
  `iommu` varchar(255) DEFAULT NULL,
  `mode` varchar(255) DEFAULT NULL,
  `nps` int NOT NULL,
  `os` varchar(255) DEFAULT NULL,
  `preferred_io` varchar(255) DEFAULT NULL,
  `smt` varchar(255) DEFAULT NULL,
  `soc_pstate` varchar(255) DEFAULT NULL,
  `x2apic` varchar(255) DEFAULT NULL,
  `other` varchar(255) DEFAULT NULL,
  `edc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`app_name`,`cpu`,`run_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `FKt4v0rrweyk393bdgt107vdx0x` (`role_id`),
  KEY `FKgd3iendaoyh04b95ykqise6qh` (`user_id`),
  CONSTRAINT `FKgd3iendaoyh04b95ykqise6qh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKt4v0rrweyk393bdgt107vdx0x` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5lwtbncug84d4ero33v3cfxvl` (`user_id`),
  CONSTRAINT `FK5lwtbncug84d4ero33v3cfxvl` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;