package br.ufscar.dc.dsw.PESCD.controllers;

import br.ufscar.dc.dsw.PESCD.dtos.OfertaListagemDto;
import br.ufscar.dc.dsw.PESCD.services.OfertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final OfertaService ofertaService;

    public AlunoController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    @GetMapping("/me/ofertas")
    public ResponseEntity<List<OfertaListagemDto>> listarOfertasDoAluno(
            @AuthenticationPrincipal UserDetails usuarioAutenticado) {

        var ofertas = ofertaService.listarOfertasDoAlunoLogado(usuarioAutenticado.getUsername());
        return ResponseEntity.ok(ofertas);
    }
}
