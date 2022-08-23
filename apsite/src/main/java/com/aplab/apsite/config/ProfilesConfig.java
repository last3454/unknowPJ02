package com.aplab.apsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ProfilesConfig {

	@Bean
	@Profile("local")
	public void localConfig() {
		log.debug("####################################");
		log.debug("system profile local loader");
		log.debug("####################################");
	}
	
	@Bean
	@Profile("dev")
	public void devConfig() {
		log.debug("####################################");
		log.debug("system profile development");
		log.debug("####################################");
	}	
	
	@Bean
	@Profile("prd")
	public void prdConfig() {
		log.debug("####################################");
		log.debug("system profile production");
		log.debug("####################################");
	}	
	
	@Bean
	@Profile("stg")
	public void stgConfig() {
		log.debug("####################################");
		log.debug("system profile stage");
		log.debug("####################################");
	}	
}
