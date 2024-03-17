package br.com.postech.produtos.drivers.external;

import br.com.postech.produtos.core.entities.Produto;

import java.util.Optional;

public interface ProdutoGateway extends Gateway<Produto> {
    Optional<Produto> buscarPorId(Long id);

}
