package com.gerenciamento_hospitalar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI GerenciamentoHospitalarDocs() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de gerenciamento hospitalar")
                        .description("Api para uso interno de funcionários e médicos com foco em gerenciamento de consultas.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Vitor de Souza Oliveira")
                                .email("vitor16.souzaoliver@gmail.com")));
    }
}
