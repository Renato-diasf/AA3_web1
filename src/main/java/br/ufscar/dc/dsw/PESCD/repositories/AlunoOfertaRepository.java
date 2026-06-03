package br.ufscar.dc.dsw.PESCD.repositories;

import br.ufscar.dc.dsw.PESCD.models.AlunoOfertaModel;
import br.ufscar.dc.dsw.PESCD.models.StatusAlunoOferta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlunoOfertaRepository extends JpaRepository<AlunoOfertaModel, UUID> {
    List<AlunoOfertaModel> findByAlunoId(UUID alunoId);

    @EntityGraph(attributePaths = {"aluno", "oferta"})
    List<AlunoOfertaModel> findByOfertaId(UUID ofertaId);

    @EntityGraph(attributePaths = {"aluno", "oferta", "logsStatus"})
    Optional<AlunoOfertaModel> findWithDetalhesById(UUID id);

    @EntityGraph(attributePaths = {"aluno", "oferta", "oferta.professorResponsavel", "planoTrabalho"})
    Optional<AlunoOfertaModel> findByAlunoUsernameAndOfertaId(String username, UUID ofertaId);

    List<AlunoOfertaModel> findByStatus(StatusAlunoOferta status);

    boolean existsByAlunoIdAndOfertaId(UUID alunoId, UUID ofertaId);
}
