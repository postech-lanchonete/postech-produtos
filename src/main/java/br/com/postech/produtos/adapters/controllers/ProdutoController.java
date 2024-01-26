package br.com.postech.produtos.adapters.controllers;

import br.com.postech.produtos.business.usecases.UseCase;
import br.com.postech.produtos.core.entities.Produto;
import br.com.postech.produtos.core.enums.CategoriaProduto;
import br.com.postech.produtos.drivers.web.ProdutoAPI;
import br.com.postech.produtos.adapters.adapter.ProdutoAdapter;
import br.com.postech.produtos.adapters.dto.CriacaoProdutoDTO;
import br.com.postech.produtos.adapters.dto.EdicaoProdutoDTO;
import br.com.postech.produtos.adapters.dto.ProdutoResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
public class ProdutoController implements ProdutoAPI {

    private final UseCase<Long, Produto> produtoBuscarPorIdUseCase;
    private final UseCase<Produto, Produto> produtoEditarUseCase;
    private final UseCase<Produto, Produto> produtoCriarUseCase;
    private final UseCase<Produto, List<Produto>> produtoBuscarTodosUseCase;
    private final ProdutoAdapter produtoAdapter;

    public ProdutoController(@Qualifier("produtoBuscarPorIdUseCase") UseCase<Long, Produto> produtoBuscarPorIdUseCase,
                             @Qualifier("produtoEditarUseCase") UseCase<Produto, Produto> produtoEditarUseCase,
                             @Qualifier("produtoCriarUseCase") UseCase<Produto, Produto> produtoCriarUseCase,
                             @Qualifier("produtoBuscarTodosUseCase") UseCase<Produto, List<Produto>> produtoBuscarTodosUseCase,
                             ProdutoAdapter produtoAdapter) {
        this.produtoBuscarPorIdUseCase = produtoBuscarPorIdUseCase;
        this.produtoEditarUseCase = produtoEditarUseCase;
        this.produtoCriarUseCase = produtoCriarUseCase;
        this.produtoBuscarTodosUseCase = produtoBuscarTodosUseCase;
        this.produtoAdapter = produtoAdapter;
    }


    @Override
    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarPorId(@PathVariable Long id) {
        Produto produto = produtoBuscarPorIdUseCase.realizar(id);
        return produtoAdapter.toDtoResponse(produto);
    }

    @Override
    @GetMapping
    public List<ProdutoResponseDTO> buscarTodos(String categoria) {
        Produto produto = new Produto();
        if(StringUtils.isNotEmpty(categoria)) {
            CategoriaProduto categoriaProduto = CategoriaProduto.encontrarEnumPorString(categoria);
            produto.setCategoria(categoriaProduto);
        }
        return produtoBuscarTodosUseCase.realizar(produto)
                .stream()
                .map(produtoAdapter::toDtoResponse)
                .toList();
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO criar(CriacaoProdutoDTO criacaoProdutoDTO) {
        Produto produto = produtoAdapter.toEntity(criacaoProdutoDTO);
        Produto produtoCriado = produtoCriarUseCase.realizar(produto);
        return produtoAdapter.toDtoResponse(produtoCriado);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProdutoResponseDTO editar(@PathVariable Long id, @RequestBody EdicaoProdutoDTO edicaoProdutoDTO) {
        Produto produto = produtoAdapter.toEntity(id, edicaoProdutoDTO);
        Produto produtoEditado = produtoEditarUseCase.realizar(produto);
        return produtoAdapter.toDtoResponse(produtoEditado);
    }
}