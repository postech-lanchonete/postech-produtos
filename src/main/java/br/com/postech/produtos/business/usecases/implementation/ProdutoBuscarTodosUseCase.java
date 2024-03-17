package br.com.postech.produtos.business.usecases.implementation;

import br.com.postech.produtos.drivers.external.ProdutoGateway;
import br.com.postech.produtos.business.usecases.UseCase;
import br.com.postech.produtos.core.entities.Produto;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("produtoBuscarTodosUseCase")
public class ProdutoBuscarTodosUseCase implements UseCase<Produto, List<Produto>> {

    private final ProdutoGateway produtoGateway;

    public ProdutoBuscarTodosUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    @Override
    public List<Produto> realizar(Produto produto) {
        return produtoGateway.buscarPor(Example.of(produto));
    }

}
