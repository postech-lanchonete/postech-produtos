package br.com.postech.produtos.business.usecases.implementation;

import br.com.postech.produtos.drivers.external.ProdutoGateway;
import br.com.postech.produtos.business.usecases.UseCase;
import br.com.postech.produtos.core.entities.Produto;
import br.com.postech.produtos.core.enums.CategoriaProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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

    @Test
    void deveAtualizarCamposNaoNulos_paraCategoria() {
        Produto produtoEdicao = new Produto();
        produtoEdicao.setId(1L);
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Nome");
        produtoExistente.setCategoria(CategoriaProduto.ACOMPANHAMENTO);
        produtoEdicao.setCategoria(CategoriaProduto.BEBIDA);
        when(buscarPorIdUseCase.realizar(1L)).thenReturn(produtoExistente);

        produtoEditarUseCase.realizar(produtoEdicao);

        assertEquals(CategoriaProduto.BEBIDA, produtoExistente.getCategoria());
    }
    @Test
    void deveAtualizarCamposNaoNulos_paraPreco() {
        Produto produtoEdicao = new Produto();
        produtoEdicao.setId(1L);
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Nome");
        produtoExistente.setPreco(BigDecimal.ONE);
        produtoEdicao.setPreco(BigDecimal.TEN);
        when(buscarPorIdUseCase.realizar(1L)).thenReturn(produtoExistente);

        produtoEditarUseCase.realizar(produtoEdicao);

        assertEquals(BigDecimal.TEN, produtoExistente.getPreco());
    }
    @Test
    void deveAtualizarCamposNaoNulos_paraDescricao() {
        Produto produtoEdicao = new Produto();
        produtoEdicao.setId(1L);
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Nome");
        produtoExistente.setDescricao("Descricao Antiga");
        produtoEdicao.setDescricao("Descricao Nova");
        when(buscarPorIdUseCase.realizar(1L)).thenReturn(produtoExistente);

        produtoEditarUseCase.realizar(produtoEdicao);

        assertEquals("Descricao Nova", produtoExistente.getDescricao());
    }
    @Test
    void deveAtualizarCamposNaoNulos_para() {
        Produto produtoEdicao = new Produto();
        produtoEdicao.setId(1L);
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Nome");
        produtoExistente.setImagem("Imagem antiga");
        produtoEdicao.setImagem("Imagem nova");
        when(buscarPorIdUseCase.realizar(1L)).thenReturn(produtoExistente);

        produtoEditarUseCase.realizar(produtoEdicao);

        assertEquals("Imagem nova", produtoExistente.getImagem());
    }

}
