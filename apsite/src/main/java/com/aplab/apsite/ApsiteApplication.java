package com.aplab.apsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

@Configuration
@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@ComponentScan(basePackages = {"com.aplab.apsite"})
public class ApsiteApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApsiteApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApsiteApplication.class, args);
	}

	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer customize() {
		//Pageable 1부터 시작
		return p -> p.setOneIndexedParameters(true);
	}
}
