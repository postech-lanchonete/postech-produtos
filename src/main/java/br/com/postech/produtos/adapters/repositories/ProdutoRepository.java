package br.com.postech.produtos.adapters.repositories;

import br.com.postech.produtos.core.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}