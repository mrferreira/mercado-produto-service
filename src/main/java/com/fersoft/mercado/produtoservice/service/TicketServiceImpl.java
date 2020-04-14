package com.fersoft.mercado.produtoservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fersoft.mercado.produtoservice.domain.Ticket;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;
	
	@Override
	@HystrixCommand(fallbackMethod = "defaultTicket")
	public Ticket findByProdutoId(Integer produtoId) {
		return restTemplate.getForObject("http://TICKET-SERVICE/ticket/produto/{produtoId}", Ticket.class, produtoId);
	}
	
	private Ticket defaultTicket(Integer produtoId) {
		return new Ticket();
	}

}
