package com.br.blog.openApiConfig;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Api Blog")
                        .version("1.0.0")
                        .description("Api for controller blogs and posts")
                        .termsOfService("https://github.com/AllisonMarquesSouza/Blog")
                        .contact(new Contact()
                                .name("Support for email")
                                .email("allisonsou10261@gmail.com"))
                        .license(new License()
                                .name("APACHE 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                ).components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                ).security(Collections.singletonList(new SecurityRequirement().addList("bearerAuth")));
    }
}