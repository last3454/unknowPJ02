package com.aplab.apsite.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//	Response XSS 방지 -> DB 데이터 특수문자 변환
//	@Bean
//	public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
//	    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().timeZone("Asia/Seoul").build();
//	    objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
//	    return new MappingJackson2HttpMessageConverter(objectMapper);
//	}

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "PUT", "POST", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(false)
            .exposedHeaders("authorization")
            .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/img/**")
    		.addResourceLocations("classpath:/public/")
    		.resourceChain(true)
    		.addResolver(new PathResourceResolver());

        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected Resource getResource(String resourcePath, Resource location) throws IOException {
                    Resource requestedResource = location.createRelative(resourcePath);
                    return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static/index.html");
                }
            });
    }
}