package com.mongodb.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan("com.mongodb")
//@EnableJpaRepositories("com.mongodb.dao.repository")
@EnableMongoRepositories("com.mongodb.dao.repository")
@SpringBootApplication
public class MongoSpringBootApplication {

	
	@Autowired
	private Environment environment;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SpringApplication.run(MongoSpringBootApplication.class, args);

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/*
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// TODO Auto-generated method stub
				//WebMvcConfigurer.super.addCorsMappings(registry);
				String allowedUrl = environment.getProperty("application.site.url");
				//registry.addMapping("/v1/**").allowedOrigins(allowedUrl);
				System.out.println("Added to registry:"+allowedUrl + ",registry:"+registry.toString());
			}
		};
	}
*/

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
	              .paths(PathSelectors.any())
	              .build()
	              .pathMapping("/");
	}

}
