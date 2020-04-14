package com.fersoft.mercado.produtoservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "TICKETS")
@Entity
public class Ticket {

	@Id
	@Column(name="TICKETID")
	private Integer ticketId;
	@Column(name="NUMEROPREMIADO")
	private Integer numeroPremiado;
	@Column(name="STATUS")
	private String status;
	@Column(name="PRODUTOID")
	private Integer produtoid;
	
	public Ticket() {
		super();
	}
	
	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", numeroPremiado=" + numeroPremiado + ", status=" + status
				+ ", produtoid=" + produtoid + "]";
	}



	public Ticket(Integer ticketId, Integer numeroPremiado, String status, Integer produtoid) {
		super();
		this.ticketId = ticketId;
		this.numeroPremiado = numeroPremiado;
		this.status = status;
		this.produtoid = produtoid;
	}

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getProdutoid() {
		return produtoid;
	}

	public void setProdutoid(Integer produtoid) {
		this.produtoid = produtoid;
	}

	public Integer getNumeroPremiado() {
		return numeroPremiado;
	}

	public void setNumeroPremiado(Integer numeroPremiado) {
		this.numeroPremiado = numeroPremiado;
	}
	
}
