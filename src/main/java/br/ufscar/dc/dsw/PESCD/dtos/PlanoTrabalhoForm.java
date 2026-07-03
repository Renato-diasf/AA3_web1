package br.ufscar.dc.dsw.PESCD.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PlanoTrabalhoForm {
    private String codigoDisciplina;
    private String nomeDisciplina;
    private String cursoDisciplina;
    private UUID professorSupervisorId;

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getCursoDisciplina() {
        return cursoDisciplina;
    }

    public void setCursoDisciplina(String cursoDisciplina) {
        this.cursoDisciplina = cursoDisciplina;
    }

    public UUID getProfessorSupervisorId() {
        return professorSupervisorId;
    }

    public void setProfessorSupervisorId(UUID professorSupervisorId) {
        this.professorSupervisorId = professorSupervisorId;
    }

    public record ResponseDto(
            MatriculaDocumentoDto matricula,
            OfertaDocumentoDto oferta,
            List<UsuarioResumoDto> supervisores,
            PlanoDto plano) {
    }

    public record PlanoDto(
            UUID id,
            String codigoDisciplina,
            String nomeDisciplina,
            String cursoDisciplina,
            UsuarioResumoDto professorSupervisor,
            String arquivoPlano,
            LocalDateTime enviadoEm) {
    }
}
