package com.results.HpcDashboard;

import com.results.HpcDashboard.models.Application;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.Benchmark;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

//			Set<Benchmark> benchmarks = new HashSet<>();
//			benchmarks.add(new Benchmark("ls-3cars","3-Cars","","cells",0,"ms","Elapsed Time","sec",1800));
//			benchmarks.add(new Benchmark("ls-car2car","Car-2-Car","ls-dyna","2.4m","cells",120,"ms","Elapsed Time","sec",900));
//			benchmarks.add(new Benchmark("ls-neon","Neon_Refined_Revised","535k","cells",150,"ms","Elapsed Time","sec",300));
//			benchmarks.add(new Benchmark("ls-odb-10m","ODB-10M","10m","cells",80,"ms","Elapsed Time","sec",86400));
//
//			Application applications = new Application("ls-dyna-9.1.3","ls-dyna","ISV","9.1.3","","","ifortran","","","mkl","","",false ,false, LocalDate.now(),false,false,false,"",benchmarks);
//			applicationRepo.save(applications);

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
				.description("")
				.build();
	}

}
