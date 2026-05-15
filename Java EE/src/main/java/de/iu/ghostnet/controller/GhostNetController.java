package de.iu.ghostnet.controller;

import de.iu.ghostnet.dto.MissingReportForm;
import de.iu.ghostnet.entity.GhostNet;
import de.iu.ghostnet.entity.RecoveryPerson;
import de.iu.ghostnet.exception.InvalidGhostNetStatusException;
import de.iu.ghostnet.service.GhostNetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GhostNetController {

    private final GhostNetService ghostNetService;

    public GhostNetController(GhostNetService ghostNetService) {
        this.ghostNetService = ghostNetService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("openNetCount", ghostNetService.countOpenGhostNets());
        return "index";
    }

    @GetMapping("/report")
    public String showReportForm(Model model) {
        if (!model.containsAttribute("ghostNet")) {
            model.addAttribute("ghostNet", new GhostNet());
        }
        return "report";
    }

    @PostMapping("/report")
    public String reportGhostNet(@Valid @ModelAttribute("ghostNet") GhostNet ghostNet,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "report";
        }

        ghostNetService.reportGhostNet(ghostNet);
        redirectAttributes.addFlashAttribute("successMessage", "Das Geisternetz wurde anonym gemeldet.");
        return "redirect:/open-nets";
    }

    @GetMapping("/open-nets")
    public String showOpenGhostNets(Model model) {
        model.addAttribute("ghostNets", ghostNetService.findOpenGhostNets());
        return "open-nets";
    }

    @GetMapping("/ghost-nets/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        addDetailsModel(id, model);
        return "details";
    }

    @PostMapping("/ghost-nets/{id}/assign")
    public String assignRecoveryPerson(@PathVariable Long id,
                                       @Valid @ModelAttribute("recoveryPerson") RecoveryPerson recoveryPerson,
                                       BindingResult bindingResult,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addDetailsModel(id, model);
            return "details";
        }

        try {
            ghostNetService.assignRecoveryPerson(id, recoveryPerson);
            redirectAttributes.addFlashAttribute("successMessage", "Die Bergung wurde übernommen.");
        } catch (InvalidGhostNetStatusException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/ghost-nets/" + id;
    }

    @PostMapping("/ghost-nets/{id}/recover")
    public String markRecovered(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ghostNetService.markRecovered(id);
            redirectAttributes.addFlashAttribute("successMessage", "Das Geisternetz wurde als geborgen markiert.");
        } catch (InvalidGhostNetStatusException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/ghost-nets/" + id;
    }

    @PostMapping("/ghost-nets/{id}/missing")
    public String markMissing(@PathVariable Long id,
                              @Valid @ModelAttribute("missingReporter") MissingReportForm missingReporter,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addDetailsModel(id, model);
            return "details";
        }

        try {
            ghostNetService.markMissing(
                    id,
                    missingReporter.getMissingReporterName(),
                    missingReporter.getMissingReporterPhone()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Das Geisternetz wurde als verschollen markiert.");
        } catch (InvalidGhostNetStatusException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ghost-nets/" + id;
    }

    @ModelAttribute("recoveryPerson")
    public RecoveryPerson recoveryPerson() {
        return new RecoveryPerson();
    }

    @ModelAttribute("missingReporter")
    public MissingReportForm missingReporter() {
        return new MissingReportForm();
    }

    private void addDetailsModel(Long id, Model model) {
        model.addAttribute("ghostNet", ghostNetService.findById(id));
    }
}
