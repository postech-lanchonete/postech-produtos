package br.com.postech.produtos.business.usecases.implementation;

import br.com.postech.produtos.drivers.external.ProdutoGateway;
import br.com.postech.produtos.core.entities.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProdutoBuscarTodosUseCaseTest {

    private ProdutoBuscarTodosUseCase produtoBuscarTodosUseCase;
    private ProdutoGateway produtoGateway;

    @BeforeEach
    void setUp() {
        produtoGateway = mock(ProdutoGateway.class);
        produtoBuscarTodosUseCase = new ProdutoBuscarTodosUseCase(produtoGateway);
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumProdutoEncontrado() {
        Produto produto = new Produto();
        when(produtoGateway.buscarPor(any(Example.class))).thenReturn(Collections.emptyList());

        List<Produto> produtosRetornados = produtoBuscarTodosUseCase.realizar(produto);

        assertEquals(Collections.emptyList(), produtosRetornados);
    }

    @Test
    void deveRetornarListaDeProdutosQuandoEncontrados() {
        Produto produto = new Produto();
        List<Produto> produtosEsperados = List.of(new Produto(), new Produto());
        when(produtoGateway.buscarPor(any(Example.class))).thenReturn(produtosEsperados);

        List<Produto> produtosRetornados = produtoBuscarTodosUseCase.realizar(produto);

        assertEquals(produtosEsperados, produtosRetornados);
    }
}
