package com.fersoft.mercado.produtoservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "PRODUTO")
@Entity
public class Produto {

	@Id
	@Column(name ="PRODUTOID")
	private Integer produtoId;
	@Column(name ="PRODUTONAME")
	private String produtoName;
	
	@Transient
	Ticket ticket;
	
	public Produto() {
		super();
	}
	
	public Integer getProdutoId() {
		return produtoId;
	}
	public void setProdutoId(Integer produtoId) {
		this.produtoId = produtoId;
	}
	public String getProdutoName() {
		return produtoName;
	}
	public void setProdutoName(String produtoName) {
		this.produtoName = produtoName;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	
	
	
	
}
