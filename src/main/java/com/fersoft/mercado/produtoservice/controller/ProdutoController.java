package com.fersoft.mercado.produtoservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fersoft.mercado.produtoservice.domain.Produto;
import com.fersoft.mercado.produtoservice.repository.ProdutoRepository;
import com.fersoft.mercado.produtoservice.service.TicketService;

@RestController
public class ProdutoController {

	@Autowired
	ProdutoRepository produtoRepository;
	@Autowired
	TicketService ticketService;
	
	@GetMapping(value="/produto")
	public Iterable<Produto> all() {
		return produtoRepository.findAll();
	}
	
	@GetMapping(value = "/produto/{produtoId}")
	public Produto findByProdutoId(@PathVariable Integer produtoId) {
		Produto produto = produtoRepository.findById(produtoId).get();
		produto.setTicket(ticketService.findByProdutoId(produtoId));
		return produto;
	}
}
