package com.fersoft.mercado.produtoservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fersoft.mercado.produtoservice.domain.Ticket;

//@FeignClient(name="ticket-service")
public interface TicketService {

	@GetMapping("/ticket/produto/{produtoId}")
	Ticket findByProdutoId(@PathVariable("produtoId") Integer produtoId);
}
