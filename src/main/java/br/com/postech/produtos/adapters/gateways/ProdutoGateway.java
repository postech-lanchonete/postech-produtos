package br.com.postech.produtos.adapters.gateways;

import br.com.postech.produtos.core.entities.Produto;

import java.util.Optional;

public interface ProdutoGateway extends Gateway<Produto> {
    Optional<Produto> buscarPorId(Long id);

}
