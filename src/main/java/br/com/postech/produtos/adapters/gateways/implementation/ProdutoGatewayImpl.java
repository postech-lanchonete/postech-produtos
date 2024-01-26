package br.com.postech.produtos.adapters.gateways.implementation;

import br.com.postech.produtos.core.entities.Produto;
import br.com.postech.produtos.adapters.gateways.ProdutoGateway;
import br.com.postech.produtos.adapters.repositories.ProdutoRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProdutoGatewayImpl implements ProdutoGateway {
    private final ProdutoRepository produtoRepository;

    public ProdutoGatewayImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    public List<Produto> buscarPor(Example<Produto> produtoExample) {
        return produtoRepository.findAll(produtoExample);
    }
}
