package br.com.avaliacao.devopscrud.config;

import br.com.avaliacao.devopscrud.produto.Produto;
import br.com.avaliacao.devopscrud.produto.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class CargaInicial {

    @Bean
    CommandLineRunner carregarDados(ProdutoRepository repository) {
        return args -> {
            repository.save(new Produto(
                    "Teclado Mecânico",
                    "Teclado USB para uso diário",
                    new BigDecimal("249.90"),
                    15
            ));

            repository.save(new Produto(
                    "Mouse",
                    "Mouse óptico USB",
                    new BigDecimal("89.90"),
                    30
            ));

            repository.save(new Produto(
                    "Monitor 24",
                    "Monitor Full HD de 24 polegadas",
                    new BigDecimal("899.00"),
                    8
            ));
        };
    }
}
