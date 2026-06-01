package br.ufscar.dc.dsw.PESCD.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(
            @RequestParam Optional<String> error,
            @RequestParam Optional<String> logout,
            Model model) {

        error.ifPresent(value -> model.addAttribute("loginErrorMessageKey", resolveLoginErrorMessage(value)));
        logout.ifPresent(value -> model.addAttribute("logoutMessageKey", "login.logout.success"));
        return "login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("dashboardTitleKey", "dashboard.admin.title");
        model.addAttribute("dashboardDescriptionKey", "dashboard.admin.description");
        return "dashboard";
    }

    @GetMapping("/secretario/dashboard")
    public String secretarioDashboard(Model model) {
        model.addAttribute("dashboardTitleKey", "dashboard.secretario.title");
        model.addAttribute("dashboardDescriptionKey", "dashboard.secretario.description");
        return "dashboard";
    }

    @GetMapping("/aluno/dashboard")
    public String alunoDashboard(Model model) {
        model.addAttribute("dashboardTitleKey", "dashboard.aluno.title");
        model.addAttribute("dashboardDescriptionKey", "dashboard.aluno.description");
        return "dashboard";
    }

    @GetMapping("/supervisor/dashboard")
    public String supervisorDashboard(Model model) {
        model.addAttribute("dashboardTitleKey", "dashboard.supervisor.title");
        model.addAttribute("dashboardDescriptionKey", "dashboard.supervisor.description");
        return "dashboard";
    }

    @GetMapping("/responsavel/dashboard")
    public String responsavelDashboard(Model model) {
        model.addAttribute("dashboardTitleKey", "dashboard.responsavel.title");
        model.addAttribute("dashboardDescriptionKey", "dashboard.responsavel.description");
        return "dashboard";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }

    private String resolveLoginErrorMessage(String error) {
        return switch (error) {
            case "disabled" -> "login.error.disabled";
            case "role" -> "login.error.role";
            default -> "login.error.invalid";
        };
    }
}
