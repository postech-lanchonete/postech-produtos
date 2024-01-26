package br.com.postech.produtos.core.enums;

import br.com.postech.produtos.business.exceptions.BadRequestException;

import java.util.Arrays;

public enum CategoriaProduto {

    LANCHE, ACOMPANHAMENTO, BEBIDA, SOBREMESA;

    public static CategoriaProduto encontrarEnumPorString(String valor) {
        return Arrays.stream(CategoriaProduto.values())
                .filter(enumValue -> enumValue.name().equals(valor.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(String.format("CategoriaProduto não encontrado para o valor: %s", valor)));
    }
}
