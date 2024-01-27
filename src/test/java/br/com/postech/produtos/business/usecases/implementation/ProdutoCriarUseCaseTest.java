package br.com.postech.produtos.business.usecases.implementation;

import br.com.postech.produtos.adapters.gateways.ProdutoGateway;
import br.com.postech.produtos.core.entities.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProdutoCriarUseCaseTest {

    private ProdutoCriarUseCase produtoCriarUseCase;
    private ProdutoGateway produtoGateway;

    @BeforeEach
    void setUp() {
        produtoGateway = mock(ProdutoGateway.class);
        produtoCriarUseCase = new ProdutoCriarUseCase(produtoGateway);
    }

    @Test
    void deveCriarProdutoQuandoNaoExisteDuplicidade() {
        Produto produto = new Produto();
        when(produtoGateway.buscarPor(any(Example.class))).thenReturn(Collections.emptyList());

        produtoCriarUseCase.realizar(produto);

        verify(produtoGateway, times(1)).salvar(produto);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoDuplicado() {
        Produto produto = new Produto();
        when(produtoGateway.buscarPor(any(Example.class))).thenReturn(Collections.singletonList(new Produto()));

        assertThrows(DataIntegrityViolationException.class, () -> produtoCriarUseCase.realizar(produto));

        verify(produtoGateway, never()).salvar(any(Produto.class));
    }
}
