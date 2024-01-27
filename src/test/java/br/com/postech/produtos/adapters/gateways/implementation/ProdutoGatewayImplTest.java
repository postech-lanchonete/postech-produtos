package br.com.postech.produtos.adapters.gateways.implementation;

import br.com.postech.produtos.adapters.gateways.ProdutoGateway;
import br.com.postech.produtos.adapters.repositories.ProdutoRepository;
import br.com.postech.produtos.core.entities.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProdutoGatewayImplTest {

    private ProdutoGateway produtoGateway;
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        produtoRepository = mock(ProdutoRepository.class);
        produtoGateway = new ProdutoGatewayImpl(produtoRepository);
    }

    @Test
    void deveSalvarProdutoComSucesso() {
        Produto produto = new Produto();
        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto resultado = produtoGateway.salvar(produto);

        assertEquals(produto, resultado);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        Long id = 1L;
        Produto produto = new Produto();
        produto.setId(id);
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoGateway.buscarPorId(id);

        assertEquals(Optional.of(produto), resultado);
        verify(produtoRepository, times(1)).findById(id);
    }

    @Test
    void deveBuscarProdutosPorExampleComSucesso() {
        Example<Produto> produtoExample = Example.of(new Produto());
        List<Produto> produtos = new ArrayList<>();
        when(produtoRepository.findAll(produtoExample)).thenReturn(produtos);

        List<Produto> resultado = produtoGateway.buscarPor(produtoExample);

        assertEquals(produtos, resultado);
        verify(produtoRepository, times(1)).findAll(produtoExample);
    }
}
