package br.com.postech.produtos.integrationTest;

import br.com.postech.produtos.adapters.dto.CriacaoProdutoDTO;
import br.com.postech.produtos.adapters.repositories.ProdutoRepository;
import br.com.postech.produtos.core.entities.Produto;
import br.com.postech.produtos.core.enums.CategoriaProduto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase
class ProdutoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        // Limpar dados de teste antes de cada execução de teste
        produtoRepository.deleteAll();
    }

    @Test
    void buscarPorId_DeveRetornarProdutoExistente() throws Exception {
        Produto produto = criarProduto(1L, "Hambúrguer", CategoriaProduto.LANCHE, BigDecimal.valueOf(32.5)
        );

        mockMvc.perform(get("/v1/produtos/{id}", produto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(produto.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(produto.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria", Matchers.is(produto.getCategoria().name())))
                .andExpect(jsonPath("$.preco", is(produto.getPreco().doubleValue())))
                .andExpect(jsonPath("$.descricao", is(produto.getDescricao())))
                .andExpect(jsonPath("$.imagem", is(produto.getImagem())));
    }

    @Test
    void buscarPorId_DeveRetornar404QuandoProdutoNaoExistir() throws Exception {
        mockMvc.perform(get("/v1/produtos/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorCategoria_DeveRetornarProdutosDaCategoria() throws Exception {
        criarProduto(1L, "Hambúrguer", CategoriaProduto.LANCHE, BigDecimal.valueOf(32.5)
        );
        criarProduto(2L, "Batata Frita", CategoriaProduto.ACOMPANHAMENTO, BigDecimal.valueOf(15.0)
        );

        mockMvc.perform(get("/v1/produtos?categoria={categoria}", "LANCHE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].categoria", is("LANCHE")));
    }

    @Test
    void buscarPorCategoria_DeveRetornarListaVaziaQuandoCategoriaNaoExistir() throws Exception {
        mockMvc.perform(get("/v1/produtos?categoria={categoria}", "SOBREMESA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void criar_DeveRetornarProdutoCriado() throws Exception {
        CriacaoProdutoDTO dto = new CriacaoProdutoDTO();
        dto.setNome("Hambúrguer");
        dto.setCategoria(CategoriaProduto.LANCHE);
        dto.setPreco(BigDecimal.valueOf(32.5));
        dto.setDescricao("Lorem ipsum dolor sit amet.");
        dto.setImagem("");

        mockMvc.perform(post("/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(dto.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria", Matchers.is(dto.getCategoria().name())))
                .andExpect(jsonPath("$.preco", is(dto.getPreco().doubleValue())))
                .andExpect(jsonPath("$.descricao", is(dto.getDescricao())))
                .andExpect(jsonPath("$.imagem", is(dto.getImagem())));

        List<Produto> produtos = produtoRepository.findAll();
        assertThat(produtos).hasSize(1);
        Produto produtoSalvo = produtos.get(0);
        assertThat(produtoSalvo.getNome()).isEqualTo(dto.getNome());
        Assertions.assertThat(produtoSalvo.getCategoria()).isEqualTo(dto.getCategoria());
        assertThat(produtoSalvo.getPreco()).isEqualTo(dto.getPreco());
        assertThat(produtoSalvo.getDescricao()).isEqualTo(dto.getDescricao());
        assertThat(produtoSalvo.getImagem()).isEqualTo(dto.getImagem());
    }

    @Test
    void criar_DeveRetornar400QuandoNomeNaoInformado() throws Exception {
        CriacaoProdutoDTO dto = new CriacaoProdutoDTO();
        dto.setCategoria(CategoriaProduto.LANCHE);
        dto.setPreco(BigDecimal.valueOf(32.5));
        dto.setDescricao("Lorem ipsum dolor sit amet.");
        dto.setImagem("");

        mockMvc.perform(post("/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criar_DeveRetornar400QuandoCategoriaNaoInformada() throws Exception {
        CriacaoProdutoDTO dto = new CriacaoProdutoDTO();
        dto.setNome("Hambúrguer");
        dto.setPreco(BigDecimal.valueOf(32.5));
        dto.setDescricao("Lorem ipsum dolor sit amet.");
        dto.setImagem("");

        mockMvc.perform(post("/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criar_DeveRetornar400QuandoPrecoNaoInformado() throws Exception {
        CriacaoProdutoDTO dto = new CriacaoProdutoDTO();
        dto.setNome("Hambúrguer");
        dto.setCategoria(CategoriaProduto.LANCHE);
        dto.setDescricao("Lorem ipsum dolor sit amet.");
        dto.setImagem("");

        mockMvc.perform(post("/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criar_DeveRetornar422_AoInserirProdutoComMesmoNomeJaExistente() throws Exception {
        Produto produto = new Produto();
        produto.setNome("Hambúrguer");
        produtoRepository.save(produto);

        CriacaoProdutoDTO dto = new CriacaoProdutoDTO();
        dto.setNome("Hambúrguer");
        dto.setCategoria(CategoriaProduto.LANCHE);
        dto.setDescricao("Lorem ipsum dolor sit amet.");
        dto.setImagem("");
        dto.setPreco(BigDecimal.ONE);

        mockMvc.perform(post("/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void editar_DeveRetornarProdutoEditado() throws Exception {
        Produto produto = criarProduto(1L, "Hambúrguer", CategoriaProduto.LANCHE, BigDecimal.valueOf(32.5));

        CriacaoProdutoDTO dto = new CriacaoProdutoDTO();
        dto.setNome("XXX");

        mockMvc.perform(put("/v1/produtos/{id}", produto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(produto.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(dto.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria", Matchers.is(produto.getCategoria().name())))
                .andExpect(jsonPath("$.preco", is(produto.getPreco().doubleValue())))
                .andExpect(jsonPath("$.descricao", is(produto.getDescricao())))
                .andExpect(jsonPath("$.imagem", is(produto.getImagem())));

        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);
        assertThat(produtoAtualizado).isNotNull();
        assertThat(produtoAtualizado.getNome()).isEqualTo(dto.getNome());
        Assertions.assertThat(produtoAtualizado.getCategoria()).isEqualTo(produto.getCategoria());
        assertThat(produtoAtualizado.getPreco()).isEqualTo(produto.getPreco());
        assertThat(produtoAtualizado.getDescricao()).isEqualTo(produto.getDescricao());
        assertThat(produtoAtualizado.getImagem()).isEqualTo(produto.getImagem());
    }

    @Test
    void editar_DeveRetornar404QuandoProdutoNaoExistir() throws Exception {
        CriacaoProdutoDTO dto = new CriacaoProdutoDTO();
        dto.setNome("XXX");

        mockMvc.perform(put("/v1/produtos/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    private Produto criarProduto(Long id, String nome, CategoriaProduto categoria, BigDecimal preco) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setCategoria(categoria);
        produto.setPreco(preco);
        produto.setDescricao("Lorem ipsum dolor sit amet.");
        produto.setImagem("");
        return produtoRepository.save(produto);
    }

}
