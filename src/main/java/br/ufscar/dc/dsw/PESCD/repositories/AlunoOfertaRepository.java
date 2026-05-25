package br.ufscar.dc.dsw.PESCD.repositories;

import br.ufscar.dc.dsw.PESCD.models.AlunoOfertaModel;
import br.ufscar.dc.dsw.PESCD.models.StatusAlunoOferta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AlunoOfertaRepository extends JpaRepository<AlunoOfertaModel, UUID> {
    List<AlunoOfertaModel> findByAlunoId(UUID alunoId);

    List<AlunoOfertaModel> findByOfertaId(UUID ofertaId);

    List<AlunoOfertaModel> findByStatus(StatusAlunoOferta status);

    boolean existsByAlunoIdAndOfertaId(UUID alunoId, UUID ofertaId);
}
