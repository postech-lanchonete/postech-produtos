package br.com.postech.produtos.business.usecases.implementation;

import br.com.postech.produtos.drivers.external.ProdutoGateway;
import br.com.postech.produtos.business.exceptions.NotFoundException;
import br.com.postech.produtos.core.entities.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProdutoBuscarPorIdUseCaseTest {

    private ProdutoBuscarPorIdUseCase produtoBuscarPorIdUseCase;
    private ProdutoGateway produtoGateway;

    @BeforeEach
    void setUp() {
        produtoGateway = mock(ProdutoGateway.class);
        produtoBuscarPorIdUseCase = new ProdutoBuscarPorIdUseCase(produtoGateway);
    }

    @Test
    void deveRetornarProdutoQuandoEncontrado() {
        Long produtoId = 1L;
        Produto produtoEsperado = new Produto();
        when(produtoGateway.buscarPorId(produtoId)).thenReturn(Optional.of(produtoEsperado));

        Produto produtoRetornado = produtoBuscarPorIdUseCase.realizar(produtoId);

        assertEquals(produtoEsperado, produtoRetornado);
    }

    @Test
    void deveLancarNotFoundExceptionQuandoProdutoNaoEncontrado() {
        Long produtoId = 1L;
        when(produtoGateway.buscarPorId(produtoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> produtoBuscarPorIdUseCase.realizar(produtoId));
    }
}
