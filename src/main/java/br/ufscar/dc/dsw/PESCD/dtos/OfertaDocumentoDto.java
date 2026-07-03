package br.ufscar.dc.dsw.PESCD.dtos;

import br.ufscar.dc.dsw.PESCD.models.StatusOferta;

import java.time.LocalDate;
import java.util.UUID;

public record OfertaDocumentoDto(
        UUID id,
        String nome,
        String semestre,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusOferta status,
        UsuarioResumoDto professorResponsavel) {
}
