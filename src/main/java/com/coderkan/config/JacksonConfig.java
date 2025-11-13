package com.coderkan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

	// com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of 'java.time.LocalDateTime' (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}

/*

	// recommended approach: Only modifies what you want; keeps Spring Boot defaults intact.
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
    	return new Jackson2ObjectMapperBuilderCustomizer() {
			@Override
			public void customize(Jackson2ObjectMapperBuilder builder) {
	        	JavaTimeModule javaTimeModule = new JavaTimeModule();
	        	// Use built-in ISO serializers for Java 8 date/time types. Ex: InstantSerializer (Jackson). ISO-8601 output
	        	javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
	        	javaTimeModule.addSerializer(OffsetDateTime.class, OffsetDateTimeSerializer.INSTANCE);
	        	javaTimeModule.addSerializer(ZonedDateTime.class, ZonedDateTimeSerializer.INSTANCE);
	        	builder.modules(javaTimeModule);
	        	builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			}
		};
    }


| Feature / Aspect                                  |     @Bean ObjectMapper                                              |     Jackson2ObjectMapperBuilderCustomizer                    |
| ------------------------------------------------- | ------------------------------------------------------------------- | ------------------------------------------------------------ |
|   Influence on Spring Boot auto-config            | Overrides Spring Boot’s default Jackson configuration               | Extends Spring Boot's default configuration (recommended)    |
|   Control level                                   | Full control (you configure everything)                             | Partial control (override only what you need)                |
|   Risk of breaking other Spring features          | High (MVC, WebFlux, WebClient, RestTemplate may behave differently) | Low (Boot integrates your changes safely)                    |
|   When Spring adds new default modules/features   | You won't get them automatically                                    | You automatically get all new improvements                   |
|   Ease of configuration                           | Simple but risky                                                    | More verbose but safe                                        |
|   Used across whole app?                          | Only if you wire it manually                                        | Automatically applied to all ObjectMappers created by Spring |
|   Best for                                        | Stand-alone apps, custom serialization, libraries                   | Real Spring Boot apps, REST APIs, microservices              |
|   JavaTimeModule support                          | Must register manually                                              | Added cleanly and safely                                     |
|   Date format control (ISO-8601)                  | Must configure manually                                             | Built-in support via builder                                 |
|   Recommended for Spring Boot 2.7.x               | Not recommended                                                     | Recommended                                                  |
*/