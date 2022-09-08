package cmc.mellyserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion)
    {

        Info info = new Info().title("Melly API Document").version(springdocVersion)
                .description("Melly API 문서입니다.")
                .contact(new Contact().name("Mocha").email("jemin03120111@gmail.com"));

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

}