package com.jump.envelope.entities;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Budget {
	
	@Id
	private Long idBudget;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal availableIncome=BigDecimal.ZERO; 
	
	@OneToMany(mappedBy = "budget", fetch = FetchType.LAZY)
	private List<Envelope> envelopes = new ArrayList<Envelope>();


	public Long getIdBudget() {
		return idBudget;
	}

	public void setIdBudget(Long idBudget) {
		this.idBudget = idBudget;
	}

	public BigDecimal getAvailableIncome() {
		return availableIncome;
	}

	public void setAvailableIncome(BigDecimal availableIncome) {
		this.availableIncome = availableIncome;
	}

	public List<Envelope> getEnvelopes() {
		return envelopes;
	}

	public void setEnvelopes(List<Envelope> envelopes) {
		this.envelopes = envelopes;
	}
	
}
