package br.ufscar.dc.dsw.PESCD.controllers;

import br.ufscar.dc.dsw.PESCD.controllers.support.DocumentoResponseAssembler;
import br.ufscar.dc.dsw.PESCD.dtos.DocumentacaoDocenciaForm;
import br.ufscar.dc.dsw.PESCD.dtos.PlanoTrabalhoForm;
import br.ufscar.dc.dsw.PESCD.dtos.RelatorioFinalForm;
import br.ufscar.dc.dsw.PESCD.services.DocumentacaoDocenciaService;
import br.ufscar.dc.dsw.PESCD.services.PlanoTrabalhoService;
import br.ufscar.dc.dsw.PESCD.services.RelatorioFinalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/documentos")
@SecurityRequirement(name = "bearerAuth")
public class DocumentoController {

    private final PlanoTrabalhoService planoTrabalhoService;
    private final DocumentacaoDocenciaService documentacaoDocenciaService;
    private final RelatorioFinalService relatorioFinalService;
    private final DocumentoResponseAssembler documentoResponseAssembler;

    public DocumentoController(
            PlanoTrabalhoService planoTrabalhoService,
            DocumentacaoDocenciaService documentacaoDocenciaService,
            RelatorioFinalService relatorioFinalService,
            DocumentoResponseAssembler documentoResponseAssembler) {
        this.planoTrabalhoService = planoTrabalhoService;
        this.documentacaoDocenciaService = documentacaoDocenciaService;
        this.relatorioFinalService = relatorioFinalService;
        this.documentoResponseAssembler = documentoResponseAssembler;
    }

    @GetMapping("/plano-trabalho")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> obterTelaPlanoTrabalho(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails usuarioAutenticado,
            @RequestParam UUID ofertaId) {

        return ResponseEntity.ok(
                documentoResponseAssembler.montarPlanoTrabalho(usuarioAutenticado.getUsername(), ofertaId));
    }

    @GetMapping("/docencia")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> obterTelaDocumentacaoDocencia(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails usuarioAutenticado,
            @RequestParam UUID ofertaId) {

        return ResponseEntity.ok(
                documentoResponseAssembler.montarDocumentacaoDocencia(usuarioAutenticado.getUsername(), ofertaId));
    }

    @GetMapping("/relatorio-final")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> obterTelaRelatorioFinal(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails usuarioAutenticado,
            @RequestParam UUID ofertaId) {

        return ResponseEntity.ok(
                documentoResponseAssembler.montarRelatorioFinal(usuarioAutenticado.getUsername(), ofertaId));
    }

    @PostMapping(value = "/plano-trabalho", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> enviarPlanoTrabalho(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails usuarioAutenticado,
            @RequestParam UUID ofertaId,
            @ParameterObject @Valid @ModelAttribute PlanoTrabalhoForm form,
            @RequestParam("arquivoPlano") MultipartFile arquivoPlano) {

        planoTrabalhoService.enviarPlano(usuarioAutenticado.getUsername(), ofertaId, form, arquivoPlano);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/docencia", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> enviarDocumentacaoDocencia(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails usuarioAutenticado,
            @RequestParam UUID ofertaId,
            @ParameterObject @Valid @ModelAttribute DocumentacaoDocenciaForm form,
            @RequestParam("arquivoDocumentacao") MultipartFile arquivoDocumentacao) {

        documentacaoDocenciaService.enviarDocumentacao(
                usuarioAutenticado.getUsername(),
                ofertaId,
                form,
                arquivoDocumentacao);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/relatorio-final", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> enviarRelatorioFinal(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails usuarioAutenticado,
            @RequestParam UUID ofertaId,
            @ParameterObject @Valid @ModelAttribute RelatorioFinalForm form,
            @RequestParam("arquivoRelatorio") MultipartFile arquivoRelatorio) {

        relatorioFinalService.enviarRelatorio(usuarioAutenticado.getUsername(), ofertaId, form, arquivoRelatorio);
        return ResponseEntity.noContent().build();
    }
}
