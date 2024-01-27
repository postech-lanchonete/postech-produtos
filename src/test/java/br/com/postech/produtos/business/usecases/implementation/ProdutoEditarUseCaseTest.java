package br.com.postech.produtos.business.usecases.implementation;

import br.com.postech.produtos.adapters.gateways.ProdutoGateway;
import br.com.postech.produtos.business.usecases.UseCase;
import br.com.postech.produtos.core.entities.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProdutoEditarUseCaseTest {

    private ProdutoEditarUseCase produtoEditarUseCase;
    private ProdutoGateway produtoGateway;
    private UseCase<Long, Produto> buscarPorIdUseCase;

    @BeforeEach
    void setUp() {
        produtoGateway = mock(ProdutoGateway.class);
        buscarPorIdUseCase = mock(UseCase.class);
        produtoEditarUseCase = new ProdutoEditarUseCase(produtoGateway, buscarPorIdUseCase);
    }

    @Test
    void deveEditarProdutoQuandoExistir() {
        Produto produtoEdicao = new Produto();
        produtoEdicao.setId(1L);
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        when(buscarPorIdUseCase.realizar(1L)).thenReturn(produtoExistente);

        produtoEditarUseCase.realizar(produtoEdicao);

        verify(produtoGateway, times(1)).salvar(produtoExistente);
    }

    @Test
    void deveAtualizarCamposNaoNulos() {
        Produto produtoEdicao = new Produto();
        produtoEdicao.setId(1L);
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Antigo Nome");
        produtoEdicao.setNome("Novo Nome");
        when(buscarPorIdUseCase.realizar(1L)).thenReturn(produtoExistente);

        produtoEditarUseCase.realizar(produtoEdicao);

        assertEquals("Novo Nome", produtoExistente.getNome());
    }

}
