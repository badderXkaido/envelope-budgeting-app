package com.jump.envelope.web;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jump.envelope.services.BudgetService;

@Controller
public class HomeController {

    private final BudgetService service;

    public HomeController(BudgetService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("budget", service.getOrCreateBudget());
        model.addAttribute("envelopes", service.listEnvelopes());
        return "index";
    }

    @PostMapping("/income")
    public String addIncome(@RequestParam BigDecimal amount, RedirectAttributes ra) {
        service.addIncome(amount);
        ra.addFlashAttribute("success", "Income added.");
        return "redirect:/";
    }

    @PostMapping("/envelopes")
    public String createEnvelope(@RequestParam String name, RedirectAttributes ra) {
        service.createEnvelope(name);
        ra.addFlashAttribute("success", "Envelope created.");
        return "redirect:/";
    }

    @PostMapping("/allocate")
    public String allocate(@RequestParam Long envelopeId, @RequestParam BigDecimal amount, RedirectAttributes ra) {
        service.allocate(envelopeId, amount);
        ra.addFlashAttribute("success", "Allocated successfully.");
        return "redirect:/";
    }


    @PostMapping("/spend")
    public String spend(@RequestParam Long envelopeId,
                        @RequestParam BigDecimal amount,
                        RedirectAttributes ra) {

    	BigDecimal spended = service.spend(envelopeId, amount);
        ra.addFlashAttribute("success", "Spend recorded.");
        ra.addFlashAttribute("spendedFromBalance", spended);
        return "redirect:/";
    }
    
    /*
     * without the GlobalExceptionHandler @ControllerAdvice
     * 
     *  
     * @PostMapping("/spend")
    public String spend(@RequestParam Long envelopeId,
                        @RequestParam BigDecimal amount,
                        RedirectAttributes ra) {
        try {
        	BigDecimal spended = service.spend(envelopeId, amount);
            ra.addFlashAttribute("success", "Spend recorded.");
            ra.addFlashAttribute("spendedFromBalance", spended);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }*/

    
    
    /*
      
    
    private String run(Runnable action, RedirectAttributes ra, String okMessage) {
        try {
            action.run();
            ra.addFlashAttribute("success", okMessage);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }*/
}
