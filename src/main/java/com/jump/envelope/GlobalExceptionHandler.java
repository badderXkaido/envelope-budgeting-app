package com.jump.envelope;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    // For validation/business-rule errors (the service throws these)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex,
                                        HttpServletRequest request,
                                        RedirectAttributes ra) {

        ra.addFlashAttribute("error", ex.getMessage());

        // Avoid redirect loops if the error happens on GET "/"
        if ("GET".equalsIgnoreCase(request.getMethod()) && "/".equals(request.getRequestURI())) {
            return "index"; // optional; usually won't happen a lot be just in case
        }
        return "redirect:/";
    }

    // For unexpected errors
    @ExceptionHandler(Exception.class)
    public String handleAny(Exception ex,
                            HttpServletRequest request,
                            RedirectAttributes ra) {

        // In real apps: log ex. For screening, keep UI message simple.
        ra.addFlashAttribute("error", "Unexpected error occurred.");

        // redirect back to home
        return "redirect:/";
    }
}
