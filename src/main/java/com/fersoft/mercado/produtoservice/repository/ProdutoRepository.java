package com.fersoft.mercado.produtoservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.fersoft.mercado.produtoservice.domain.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Integer> {

}
