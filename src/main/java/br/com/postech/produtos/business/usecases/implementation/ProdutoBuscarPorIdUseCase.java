package br.com.postech.produtos.business.usecases.implementation;

import br.com.postech.produtos.drivers.external.ProdutoGateway;
import br.com.postech.produtos.business.exceptions.NotFoundException;
import br.com.postech.produtos.business.usecases.UseCase;
import br.com.postech.produtos.core.entities.Produto;
import org.springframework.stereotype.Component;

@Component("produtoBuscarPorIdUseCase")
public class ProdutoBuscarPorIdUseCase implements UseCase<Long, Produto> {

    private final ProdutoGateway produtoGateway;

    public ProdutoBuscarPorIdUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    @Override
    public Produto realizar(Long id) {
        return produtoGateway.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException(String.format("Produto não encontrado com o id %d", id)));
    }

}
