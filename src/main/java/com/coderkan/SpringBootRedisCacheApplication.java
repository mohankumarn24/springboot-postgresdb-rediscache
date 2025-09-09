package com.coderkan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class SpringBootRedisCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisCacheApplication.class, args);
	}
}

/**
 * Steps:
 * 	Run: D:\dev\github\springboot-postgresdb-rediscache\Redis-x64-3.0.504\redis-server.exe
 * SwaggerURL: http://localhost:8080/swagger-ui.html
 * Observe data changes in Redis Desktop Manager and DBeaver (PostgreSQL)
 */