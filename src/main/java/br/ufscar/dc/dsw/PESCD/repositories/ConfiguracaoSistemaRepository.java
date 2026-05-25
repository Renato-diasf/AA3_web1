package br.ufscar.dc.dsw.PESCD.repositories;

import br.ufscar.dc.dsw.PESCD.models.ConfiguracaoSistemaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ConfiguracaoSistemaRepository extends JpaRepository<ConfiguracaoSistemaModel, UUID> {
    ConfiguracaoSistemaModel findByChave(String chave);
}
