package br.com.postech.produtos.adapters.adapter;

import br.com.postech.produtos.core.entities.Produto;
import br.com.postech.produtos.adapters.dto.CriacaoProdutoDTO;
import br.com.postech.produtos.adapters.dto.EdicaoProdutoDTO;
import br.com.postech.produtos.adapters.dto.ProdutoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoAdapter {

    @Mapping(target = "id", ignore = true)
    Produto toEntity(CriacaoProdutoDTO dto);

    Produto toEntity(Long id, EdicaoProdutoDTO dto);

    ProdutoResponseDTO toDtoResponse(Produto produto);
}
