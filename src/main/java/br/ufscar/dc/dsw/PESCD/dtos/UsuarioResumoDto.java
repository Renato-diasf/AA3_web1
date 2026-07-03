package br.ufscar.dc.dsw.PESCD.dtos;

import java.util.UUID;

public record UsuarioResumoDto(
        UUID id,
        String nomeCompleto,
        String email,
        String ra,
        String username) {
}
