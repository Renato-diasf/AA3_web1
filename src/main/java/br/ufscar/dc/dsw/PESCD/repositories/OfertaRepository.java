package br.ufscar.dc.dsw.PESCD.repositories;

import br.ufscar.dc.dsw.PESCD.models.OfertaModel;
import br.ufscar.dc.dsw.PESCD.models.StatusOferta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfertaRepository extends JpaRepository<OfertaModel, UUID> {
    @EntityGraph(attributePaths = {"professorResponsavel", "criadoPor"})
    List<OfertaModel> findByOrderBySemestreDesc();

    @EntityGraph(attributePaths = {"professorResponsavel", "criadoPor", "alunos", "alunos.aluno"})
    Optional<OfertaModel> findWithDetalhesById(UUID id);

    List<OfertaModel> findByStatus(StatusOferta status);

    List<OfertaModel> findByProfessorResponsavelIdOrderBySemestreDesc(UUID professorResponsavelId);
}
