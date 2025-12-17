package com.jump.envelope.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jump.envelope.entities.Budget;
import com.jump.envelope.entities.Envelope;
import com.jump.envelope.repositories.BudgetRepository;
import com.jump.envelope.repositories.EnvelopeRepository;

@Service
public class BudgetService {

    private static final long SINGLE_BUDGET_ID = 1L;

    private final BudgetRepository budgetRepo;
    private final EnvelopeRepository envelopeRepo;

    public BudgetService(BudgetRepository budgetRepo, EnvelopeRepository envelopeRepo) {
        this.budgetRepo = budgetRepo;
        this.envelopeRepo = envelopeRepo;
    }

    public Budget getOrCreateBudget() {
    	Budget budget = budgetRepo.findById(SINGLE_BUDGET_ID).orElse(null);

        if (budget == null) {
            budget = new Budget();
            budget.setIdBudget(SINGLE_BUDGET_ID);
            budget.setAvailableIncome(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            budget = budgetRepo.save(budget);
        }

        return budget;
    }

    public List<Envelope> listEnvelopes() {
        return envelopeRepo.findByBudget_IdBudget(SINGLE_BUDGET_ID);
    }

    @Transactional
    public void addIncome(BigDecimal amount) {
        amount = normalize(amount);
        requirePositive(amount);

        Budget budget = getOrCreateBudget();
        budget.setAvailableIncome(budget.getAvailableIncome().add(amount));
        budgetRepo.save(budget);
    }

    @Transactional
    public Envelope createEnvelope(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Envelope name is required.");
        }

        Budget budget = getOrCreateBudget();
        String clean = name.strip();

        if (envelopeRepo.existsByBudget_IdBudgetAndNameIgnoreCase(budget.getIdBudget(), clean)) {
            throw new IllegalArgumentException("Envelope already exists.");
        }

        Envelope env = new Envelope();
        env.setName(clean);
        env.setBalance(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        env.setBudget(budget); // IMPORTANT: budget_id is NOT NULL

        return envelopeRepo.save(env);
    }

    @Transactional
    public void allocate(Long envelopeId, BigDecimal amount) {
        amount = normalize(amount);
        requirePositive(amount);

        Budget budget = getOrCreateBudget();

        if (budget.getAvailableIncome().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough available income to allocate.");
        }

        Optional<Envelope> opt = envelopeRepo.findByIdAndBudget_IdBudget(envelopeId, SINGLE_BUDGET_ID);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Envelope not found.");
        }

        Envelope env = opt.get();
        
        budget.setAvailableIncome(budget.getAvailableIncome().subtract(amount));
        env.setBalance(env.getBalance().add(amount));

        budgetRepo.save(budget);
        envelopeRepo.save(env);
    }

    @Transactional
    public BigDecimal spend(Long envelopeId, BigDecimal amount) {
        amount = normalize(amount);
        requirePositive(amount);

        Optional<Envelope> opt = envelopeRepo.findByIdAndBudget_IdBudget(envelopeId, SINGLE_BUDGET_ID);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Envelope not found.");
        }
        Envelope env = opt.get();
        
        if (env.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough funds in this envelope.");
        }

        env.setBalance(env.getBalance().subtract(amount));
        envelopeRepo.save(env);
        return amount;
    }

    private static BigDecimal normalize(BigDecimal amount) {
        if (amount == null) throw new IllegalArgumentException("Amount is required.");
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private static void requirePositive(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
    }
}
