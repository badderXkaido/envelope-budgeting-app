package com.jump.envelope;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jump.envelope.services.BudgetService;

@SpringBootApplication
public class EnvelopeBudgetApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnvelopeBudgetApplication.class, args);
	}
	@Bean
	CommandLineRunner seed(BudgetService service) {
	    return args -> {
	        service.getOrCreateBudget();
	        if (service.listEnvelopes().isEmpty()) {
	            service.createEnvelope("Rent");
	            service.createEnvelope("Groceries");
	            service.createEnvelope("Fun");
	        }
	    };
	}


}
