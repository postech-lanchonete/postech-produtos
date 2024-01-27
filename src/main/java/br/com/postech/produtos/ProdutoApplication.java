package br.com.postech.produtos;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProdutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProdutoApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title("Postech - Lanchonete do Bairro 🍔 - Produtos")
						.description("Fundamental para o cardápio, a API de Produtos permite a criação e busca eficiente de produtos em categorias específicas, como lanches, acompanhamentos, bebidas e sobremesas.")
						.contact(new Contact().name("Daniel Maria da Silva").url("https://github.com/postech-lanchonete"))
						.license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"))
						.version("1.0.0-POC"));
	}
}
