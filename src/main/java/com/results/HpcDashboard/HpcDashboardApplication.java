package com.results.HpcDashboard;

import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.repo.CPURepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class HpcDashboardApplication implements CommandLineRunner {

	@Autowired
	ApplicationRepo applicationRepo;

	@Autowired
	BenchmarkRepo benchmarkRepo;

	@Autowired
	CPURepo cpuRepo;

		@Override
	public void run(String... args) throws Exception {
//			applicationsRepo.save(new Applications("ls-dyna","9.1.3","","","ifortran","","mkl","","",false ,false,LocalDate.now(),false,false,false,""));
//			benchmarksRepo.save(new Benchmarks("ls-3cars","3-Cars","ls-dyna","","cells",0,"ms","Elapsed Time","sec",1800));
//			benchmarksRepo.save(new Benchmarks("ls-car2car","Car-2-Car","ls-dyna","2.4m","cells",120,"ms","Elapsed Time","sec",900));
//			benchmarksRepo.save(new Benchmarks("ls-neon","Neon_Refined_Revised","ls-dyna","535k","cells",150,"ms","Elapsed Time","sec",300));
//			benchmarksRepo.save(new Benchmarks("ls-odb-10m","ODB-10M","ls-dyna","10m","cells",80,"ms","Elapsed Time","sec",86400));

//			Set<Benchmarks> benchmarks = new HashSet<>();
//			benchmarks.add(new Benchmarks("ls-3cars","3-Cars","","cells",0,"ms","Elapsed Time","sec",1800));
//			benchmarks.add(new Benchmarks("ls-car2car","Car-2-Car","ls-dyna","2.4m","cells",120,"ms","Elapsed Time","sec",900));
//			benchmarks.add(new Benchmarks("ls-neon","Neon_Refined_Revised","535k","cells",150,"ms","Elapsed Time","sec",300));
//			benchmarks.add(new Benchmarks("ls-odb-10m","ODB-10M","10m","cells",80,"ms","Elapsed Time","sec",86400));
//
//			Applications applications = new Applications("ls-dyna-9.1.3","ls-dyna","9.1.3","","","ifortran","","mkl","","",false ,false,LocalDate.now(),false,false,false,"",benchmarks);
//			applicationsRepo.save(applications);

		    //cpuRepo.save(CPU.builder().cpu_generation("ROME").cpu_sku("7742").cpu_manufacturer("AMD").all_core_freq(2.7).base_freq(2.25).cores(64).ddr_channels(8).l3_cache(256).max_ddr_freq(3200).peak_freq(3.4).tdp(225).build());
			//cpuRepo.save(CPU.builder().cpu_generation("ROME").cpu_sku("7702").cpu_manufacturer("AMD").base_freq(2.0).cores(64).ddr_channels(8).l3_cache(256).max_ddr_freq(3200).peak_freq(3.35).tdp(200).build());
		}


	public static void main(String[] args) {
		SpringApplication.run(HpcDashboardApplication.class, args);
	}

	@Bean
	public Docket apiMonitoramento() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.results"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("HPC Dashboard Controller")
				.description("Services")
				.build();
	}

}
