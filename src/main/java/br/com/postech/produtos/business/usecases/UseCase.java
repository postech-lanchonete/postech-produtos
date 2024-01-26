package br.com.postech.produtos.business.usecases;

public interface UseCase<E, S> {
    S realizar(E entrada);
}
