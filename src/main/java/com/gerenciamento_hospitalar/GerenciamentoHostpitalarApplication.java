package com.gerenciamento_hospitalar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GerenciamentoHostpitalarApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerenciamentoHostpitalarApplication.class, args);
	}

}
