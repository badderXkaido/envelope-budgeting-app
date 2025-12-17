package com.jump.envelope.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jump.envelope.entities.Envelope;

public interface EnvelopeRepository extends JpaRepository<Envelope, Long>{
	
	boolean existsByNameIgnoreCase(String name);
	
	List<Envelope> findByBudget_IdBudget(Long budgetId);

    boolean existsByBudget_IdBudgetAndNameIgnoreCase(Long budgetId, String name);

    Optional<Envelope> findByIdAndBudget_IdBudget(Long id, Long budgetId);

}
