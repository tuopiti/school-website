package com.piti.java.schoolwebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI openAPI() {
	    return new OpenAPI().addSecurityItem(new SecurityRequirement().
	        addList("Swagger"))
	        .info(new Info().title("School Website API")
	            .description("Some custom description of API.")
	            .version("1.0").contact(new Contact().name("Tuo Piti")
	                .email("tuopiti36@gmail.com").url("https://www.facebook.com/pi.ti.98031"))
	            .license(new License().name("License of API")
	                .url("API license URL")));
	}
}
