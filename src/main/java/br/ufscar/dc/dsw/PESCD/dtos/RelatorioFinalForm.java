package br.ufscar.dc.dsw.PESCD.dtos;

import br.ufscar.dc.dsw.PESCD.models.StatusAlunoOferta;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RelatorioFinalForm {
    private Integer frequenciaInformada;

    public Integer getFrequenciaInformada() {
        return frequenciaInformada;
    }

    public void setFrequenciaInformada(Integer frequenciaInformada) {
        this.frequenciaInformada = frequenciaInformada;
    }

    public record ResponseDto(
            MatriculaDocumentoDto matricula,
            OfertaDocumentoDto oferta,
            PlanoTrabalhoForm.PlanoDto plano,
            RelatorioFinalDto relatorio,
            List<HistoricoStatusDto> historico) {
    }

    public record RelatorioFinalDto(
            UUID id,
            Integer frequenciaInformada,
            String arquivoRelatorio,
            LocalDateTime enviadoEm) {
    }

    public record HistoricoStatusDto(
            UUID id,
            StatusAlunoOferta statusAnterior,
            StatusAlunoOferta statusNovo,
            String observacao,
            LocalDateTime alteradoEm) {
    }
}
