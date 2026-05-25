package br.ufscar.dc.dsw.PESCD.repositories;

import br.ufscar.dc.dsw.PESCD.models.PerfilUsuario;
import br.ufscar.dc.dsw.PESCD.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    UsuarioModel findUsuarioModelByEmail(String email);

    UsuarioModel findUsuarioModelByNomeUsuario(String nomeUsuario);

    List<UsuarioModel> findByPerfil(PerfilUsuario perfil);
}
