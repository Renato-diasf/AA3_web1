package br.ufscar.dc.dsw.PESCD.controllers.support;

import br.ufscar.dc.dsw.PESCD.dtos.DocumentacaoDocenciaForm;
import br.ufscar.dc.dsw.PESCD.dtos.MatriculaDocumentoDto;
import br.ufscar.dc.dsw.PESCD.dtos.OfertaDocumentoDto;
import br.ufscar.dc.dsw.PESCD.dtos.PlanoTrabalhoForm;
import br.ufscar.dc.dsw.PESCD.dtos.RelatorioFinalForm;
import br.ufscar.dc.dsw.PESCD.dtos.UsuarioResumoDto;
import br.ufscar.dc.dsw.PESCD.exception.RecursoNaoEncontradoException;
import br.ufscar.dc.dsw.PESCD.models.AlunoOfertaModel;
import br.ufscar.dc.dsw.PESCD.models.DocumentacaoDocenciaModel;
import br.ufscar.dc.dsw.PESCD.models.LogStatusAlunoOfertaModel;
import br.ufscar.dc.dsw.PESCD.models.OfertaModel;
import br.ufscar.dc.dsw.PESCD.models.PlanoTrabalhoModel;
import br.ufscar.dc.dsw.PESCD.models.RelatorioFinalModel;
import br.ufscar.dc.dsw.PESCD.models.UsuarioModel;
import br.ufscar.dc.dsw.PESCD.repositories.AlunoOfertaRepository;
import br.ufscar.dc.dsw.PESCD.services.RelatorioFinalService;
import br.ufscar.dc.dsw.PESCD.services.UsuarioService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DocumentoResponseAssembler {

    private final AlunoOfertaRepository alunoOfertaRepository;
    private final UsuarioService usuarioService;
    private final RelatorioFinalService relatorioFinalService;

    public DocumentoResponseAssembler(
            AlunoOfertaRepository alunoOfertaRepository,
            UsuarioService usuarioService,
            RelatorioFinalService relatorioFinalService) {
        this.alunoOfertaRepository = alunoOfertaRepository;
        this.usuarioService = usuarioService;
        this.relatorioFinalService = relatorioFinalService;
    }

    public PlanoTrabalhoForm.ResponseDto montarPlanoTrabalho(String username, UUID ofertaId) {
        var matricula = buscarMatricula(username, ofertaId);
        return new PlanoTrabalhoForm.ResponseDto(
                toMatriculaDto(matricula),
                toOfertaDto(matricula.getOferta()),
                usuarioService.listarSupervisores().stream()
                        .map(this::toUsuarioDto)
                        .toList(),
                toPlanoDto(matricula.getPlanoTrabalho()));
    }

    public DocumentacaoDocenciaForm.ResponseDto montarDocumentacaoDocencia(String username, UUID ofertaId) {
        var matricula = buscarMatricula(username, ofertaId);
        return new DocumentacaoDocenciaForm.ResponseDto(
                toMatriculaDto(matricula),
                toOfertaDto(matricula.getOferta()),
                toDocumentacaoDto(matricula.getDocumentacaoDocencia()));
    }

    public RelatorioFinalForm.ResponseDto montarRelatorioFinal(String username, UUID ofertaId) {
        var matricula = buscarMatricula(username, ofertaId);
        return new RelatorioFinalForm.ResponseDto(
                toMatriculaDto(matricula),
                toOfertaDto(matricula.getOferta()),
                toPlanoDto(matricula.getPlanoTrabalho()),
                toRelatorioDto(matricula.getRelatorioFinal()),
                relatorioFinalService.listarHistorico(matricula.getId()).stream()
                        .map(this::toHistoricoDto)
                        .toList());
    }

    private AlunoOfertaModel buscarMatricula(String username, UUID ofertaId) {
        return alunoOfertaRepository.findByAlunoUsernameAndOfertaId(username, ofertaId)
                .orElseThrow(RecursoNaoEncontradoException::new);
    }

    private MatriculaDocumentoDto toMatriculaDto(AlunoOfertaModel matricula) {
        return new MatriculaDocumentoDto(
                matricula.getId(),
                matricula.getStatus(),
                matricula.getTipoCredito(),
                matricula.getFrequenciaFinal(),
                matricula.getNotaFinal() != null ? matricula.getNotaFinal().name() : null,
                matricula.getCriadoEm());
    }

    private OfertaDocumentoDto toOfertaDto(OfertaModel oferta) {
        return new OfertaDocumentoDto(
                oferta.getId(),
                oferta.getNome(),
                oferta.getSemestre(),
                oferta.getDataInicio(),
                oferta.getDataFim(),
                oferta.getStatus(),
                toUsuarioDto(oferta.getProfessorResponsavel()));
    }

    private UsuarioResumoDto toUsuarioDto(UsuarioModel usuario) {
        return new UsuarioResumoDto(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getRa(),
                usuario.getUsername());
    }

    private PlanoTrabalhoForm.PlanoDto toPlanoDto(PlanoTrabalhoModel plano) {
        if (plano == null) {
            return null;
        }
        return new PlanoTrabalhoForm.PlanoDto(
                plano.getId(),
                plano.getCodigoDisciplina(),
                plano.getNomeDisciplina(),
                plano.getCursoDisciplina(),
                toUsuarioDto(plano.getProfessorSupervisor()),
                plano.getArquivoPlano(),
                plano.getEnviadoEm());
    }

    private DocumentacaoDocenciaForm.DocumentacaoDocenciaDto toDocumentacaoDto(
            DocumentacaoDocenciaModel documentacao) {
        if (documentacao == null) {
            return null;
        }
        return new DocumentacaoDocenciaForm.DocumentacaoDocenciaDto(
                documentacao.getId(),
                documentacao.getNomeInstituicao(),
                documentacao.getNomeDisciplina(),
                documentacao.getCursoDisciplina(),
                documentacao.getCargaHoraria(),
                documentacao.getArquivoDocumentacao(),
                documentacao.getEnviadoEm());
    }

    private RelatorioFinalForm.RelatorioFinalDto toRelatorioDto(RelatorioFinalModel relatorio) {
        if (relatorio == null) {
            return null;
        }
        return new RelatorioFinalForm.RelatorioFinalDto(
                relatorio.getId(),
                relatorio.getFrequenciaInformada(),
                relatorio.getArquivoRelatorio(),
                relatorio.getEnviadoEm());
    }

    private RelatorioFinalForm.HistoricoStatusDto toHistoricoDto(LogStatusAlunoOfertaModel log) {
        return new RelatorioFinalForm.HistoricoStatusDto(
                log.getId(),
                log.getStatusAnterior(),
                log.getStatusNovo(),
                log.getObservacao(),
                log.getAlteradoEm());
    }
}
