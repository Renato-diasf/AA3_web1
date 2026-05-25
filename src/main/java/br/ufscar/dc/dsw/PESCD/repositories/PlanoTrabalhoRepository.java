package br.ufscar.dc.dsw.PESCD.repositories;

import br.ufscar.dc.dsw.PESCD.models.PlanoTrabalhoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PlanoTrabalhoRepository extends JpaRepository<PlanoTrabalhoModel, UUID> {
    PlanoTrabalhoModel findByAlunoOfertaId(UUID alunoOfertaId);

    List<PlanoTrabalhoModel> findByProfessorSupervisorId(UUID professorSupervisorId);
}
