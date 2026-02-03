package com.hold.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI holdOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Hold API")
						.description("API para gestão de Pessoas - Global Tech Holding")
						.version("v1.0.0")
						.contact(new Contact()
								.name("Suporte Hold")
								.email("gpe@gthpi.com")
								.url("https://gthpi.com"))
						.license(new License()
								.name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0")));
	}
}
