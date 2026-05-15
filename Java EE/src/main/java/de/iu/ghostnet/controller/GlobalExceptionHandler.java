package de.iu.ghostnet.controller;

import de.iu.ghostnet.exception.GhostNetNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GhostNetNotFoundException.class)
    public String handleGhostNetNotFound(GhostNetNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Geisternetz nicht gefunden");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
