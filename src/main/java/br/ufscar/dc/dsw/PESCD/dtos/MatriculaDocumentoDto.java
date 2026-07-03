package br.ufscar.dc.dsw.PESCD.dtos;

import br.ufscar.dc.dsw.PESCD.models.StatusAlunoOferta;
import br.ufscar.dc.dsw.PESCD.models.TipoCredito;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatriculaDocumentoDto(
        UUID id,
        StatusAlunoOferta status,
        TipoCredito tipoCredito,
        Integer frequenciaFinal,
        String notaFinal,
        LocalDateTime criadoEm) {
}
