package br.ufscar.dc.dsw.PESCD;

import br.ufscar.dc.dsw.PESCD.models.AlunoOfertaModel;
import br.ufscar.dc.dsw.PESCD.models.ConfiguracaoSistemaModel;
import br.ufscar.dc.dsw.PESCD.models.DocumentacaoDocenciaModel;
import br.ufscar.dc.dsw.PESCD.models.LogStatusAlunoOfertaModel;
import br.ufscar.dc.dsw.PESCD.models.OfertaModel;
import br.ufscar.dc.dsw.PESCD.models.PerfilModel;
import br.ufscar.dc.dsw.PESCD.models.PerfilUsuario;
import br.ufscar.dc.dsw.PESCD.models.PlanoTrabalhoModel;
import br.ufscar.dc.dsw.PESCD.models.StatusAlunoOferta;
import br.ufscar.dc.dsw.PESCD.models.StatusOferta;
import br.ufscar.dc.dsw.PESCD.models.TipoCredito;
import br.ufscar.dc.dsw.PESCD.models.UsuarioModel;
import br.ufscar.dc.dsw.PESCD.repositories.AlunoOfertaRepository;
import br.ufscar.dc.dsw.PESCD.repositories.ConfiguracaoSistemaRepository;
import br.ufscar.dc.dsw.PESCD.repositories.DocumentacaoDocenciaRepository;
import br.ufscar.dc.dsw.PESCD.repositories.LogStatusAlunoOfertaRepository;
import br.ufscar.dc.dsw.PESCD.repositories.OfertaRepository;
import br.ufscar.dc.dsw.PESCD.repositories.PerfilRepository;
import br.ufscar.dc.dsw.PESCD.repositories.PlanoTrabalhoRepository;
import br.ufscar.dc.dsw.PESCD.repositories.UsuarioRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class PescdApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(PescdApplication.class, args);

		var usuarioRepository = context.getBean(UsuarioRepository.class);
		var ofertaRepository = context.getBean(OfertaRepository.class);
		var alunoOfertaRepository = context.getBean(AlunoOfertaRepository.class);
		var planoTrabalhoRepository = context.getBean(PlanoTrabalhoRepository.class);
		var documentacaoDocenciaRepository = context.getBean(DocumentacaoDocenciaRepository.class);
		var logStatusRepository = context.getBean(LogStatusAlunoOfertaRepository.class);
		var configuracaoSistemaRepository = context.getBean(ConfiguracaoSistemaRepository.class);
		var perfilRepository = context.getBean(PerfilRepository.class);
		var passwordEncoder = context.getBean(PasswordEncoder.class);

		var perfilAdmin = obterOuCriarPerfil(perfilRepository, PerfilUsuario.ROLE_ADMIN);
		var perfilSecretario = obterOuCriarPerfil(perfilRepository, PerfilUsuario.ROLE_SECRETARIO);
		var perfilAluno = obterOuCriarPerfil(perfilRepository, PerfilUsuario.ROLE_ALUNO);
		var perfilSupervisor = obterOuCriarPerfil(perfilRepository, PerfilUsuario.ROLE_SUPERVISOR);
		var perfilResponsavel = obterOuCriarPerfil(perfilRepository, PerfilUsuario.ROLE_RESPONSAVEL);

		if (usuarioRepository.count() == 0) {
			var administrador = criarUsuario("Administrador PESCD", "admin@ufscar.br", "admin", "admin", passwordEncoder, perfilAdmin);
			var secretario = criarUsuario("Secretario PESCD", "secretario@ufscar.br", "secretario", "secretario", passwordEncoder, perfilSecretario);
			var professorResponsavel = criarUsuario("Professor Responsavel", "responsavel@ufscar.br", "responsavel", "responsavel", passwordEncoder, perfilResponsavel);
			var professorSupervisor = criarUsuario("Professor Supervisor", "supervisor@ufscar.br", "supervisor", "supervisor", passwordEncoder, perfilSupervisor);
			var alunoEstagio = criarUsuario("Aluno Estagio", "aluno.estagio@ufscar.br", "aluno.estagio", "123456", passwordEncoder, perfilAluno);
			var alunoDocumentacao = criarUsuario("Aluno Documentacao", "aluno.documentacao@ufscar.br", "aluno.documentacao", "654321", passwordEncoder, perfilAluno);

			usuarioRepository.save(administrador);
			usuarioRepository.save(secretario);
			usuarioRepository.save(professorResponsavel);
			usuarioRepository.save(professorSupervisor);
			usuarioRepository.save(alunoEstagio);
			usuarioRepository.save(alunoDocumentacao);

			var oferta = new OfertaModel();
			oferta.setNome("PESCD 2026/1");
			oferta.setSemestre("2026/1");
			oferta.setDataInicio(LocalDate.of(2026, 3, 1));
			oferta.setDataFim(LocalDate.of(2026, 7, 15));
			oferta.setStatus(StatusOferta.EM_ANDAMENTO);
			oferta.setProfessorResponsavel(professorResponsavel);
			oferta.setCriadoPor(secretario);
			oferta.setCriadoEm(LocalDateTime.now());
			ofertaRepository.save(oferta);

			var matriculaEstagio = criarMatricula(alunoEstagio, oferta, StatusAlunoOferta.PLANO_ENVIADO, TipoCredito.ESTAGIO);
			alunoOfertaRepository.save(matriculaEstagio);

			var plano = new PlanoTrabalhoModel();
			plano.setAlunoOferta(matriculaEstagio);
			plano.setCodigoDisciplina("1001497");
			plano.setNomeDisciplina("Desenvolvimento de Software para Web 1");
			plano.setCursoDisciplina("Ciencia da Computacao");
			plano.setProfessorSupervisor(professorSupervisor);
			plano.setArquivoPlano("uploads/planos/plano-aluno-estagio.pdf");
			plano.setEnviadoEm(LocalDateTime.now());
			planoTrabalhoRepository.save(plano);

			var matriculaDocumentacao = criarMatricula(alunoDocumentacao, oferta, StatusAlunoOferta.DOCUMENTACAO_ENVIADA, TipoCredito.DOCUMENTACAO);
			alunoOfertaRepository.save(matriculaDocumentacao);

			var documentacao = new DocumentacaoDocenciaModel();
			documentacao.setAlunoOferta(matriculaDocumentacao);
			documentacao.setNomeInstituicao("Universidade Federal de Sao Carlos");
			documentacao.setNomeDisciplina("Programacao Orientada a Objetos");
			documentacao.setCursoDisciplina("Engenharia de Computacao");
			documentacao.setCargaHoraria(60);
			documentacao.setArquivoDocumentacao("uploads/documentos/documentacao-aluno.pdf");
			documentacao.setEnviadoEm(LocalDateTime.now());
			documentacaoDocenciaRepository.save(documentacao);

			logStatusRepository.save(criarLog(matriculaEstagio, StatusAlunoOferta.NAO_ENVIADO, StatusAlunoOferta.PLANO_ENVIADO, alunoEstagio, "Plano enviado pelo aluno."));
			logStatusRepository.save(criarLog(matriculaDocumentacao, StatusAlunoOferta.NAO_ENVIADO, StatusAlunoOferta.DOCUMENTACAO_ENVIADA, alunoDocumentacao, "Documentacao enviada pelo aluno."));

			var configuracao = new ConfiguracaoSistemaModel();
			configuracao.setChave("instrucoes_encerramento_secretario");
			configuracao.setValor("Conferir notas, frequencias e documentos antes de encerrar a oferta.");
			configuracaoSistemaRepository.save(configuracao);
		}
	}

	private static PerfilModel obterOuCriarPerfil(PerfilRepository perfilRepository, PerfilUsuario perfil) {
		return perfilRepository.findByNome(perfil)
				.orElseGet(() -> perfilRepository.save(new PerfilModel(perfil)));
	}

	private static UsuarioModel criarUsuario(
			String nomeCompleto,
			String email,
			String username,
			String password,
			PasswordEncoder passwordEncoder,
			PerfilModel... perfis) {
		var usuario = new UsuarioModel();
		usuario.setNomeCompleto(nomeCompleto);
		usuario.setEmail(email);
		usuario.setUsername(username);
		usuario.setPassword(passwordEncoder.encode(password));
		usuario.setEnabled(true);
		for (PerfilModel perfil : perfis) {
			usuario.adicionarPerfil(perfil);
		}
		return usuario;
	}

	private static AlunoOfertaModel criarMatricula(UsuarioModel aluno, OfertaModel oferta, StatusAlunoOferta status, TipoCredito tipoCredito) {
		var matricula = new AlunoOfertaModel();
		matricula.setAluno(aluno);
		matricula.setOferta(oferta);
		matricula.setStatus(status);
		matricula.setTipoCredito(tipoCredito);
		matricula.setCriadoEm(LocalDateTime.now());
		return matricula;
	}

	private static LogStatusAlunoOfertaModel criarLog(
			AlunoOfertaModel alunoOferta,
			StatusAlunoOferta statusAnterior,
			StatusAlunoOferta statusNovo,
			UsuarioModel alteradoPor,
			String observacao) {
		var log = new LogStatusAlunoOfertaModel();
		log.setAlunoOferta(alunoOferta);
		log.setStatusAnterior(statusAnterior);
		log.setStatusNovo(statusNovo);
		log.setAlteradoPor(alteradoPor);
		log.setAlteradoEm(LocalDateTime.now());
		log.setObservacao(observacao);
		return log;
	}
}
