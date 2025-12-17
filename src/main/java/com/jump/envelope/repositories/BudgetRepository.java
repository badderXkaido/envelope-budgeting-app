package com.jump.envelope.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jump.envelope.entities.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long>{

	
}
