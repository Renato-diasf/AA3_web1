package br.ufscar.dc.dsw.PESCD.services;

import br.ufscar.dc.dsw.PESCD.exception.RecursoNaoEncontradoException;
import br.ufscar.dc.dsw.PESCD.exception.ValidacaoNegocioException;
import br.ufscar.dc.dsw.PESCD.models.PerfilUsuario;
import br.ufscar.dc.dsw.PESCD.models.UsuarioModel;
import br.ufscar.dc.dsw.PESCD.repositories.PerfilRepository;
import br.ufscar.dc.dsw.PESCD.repositories.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UsuarioModel obterLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new RecursoNaoEncontradoException();
        }
        return usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(RecursoNaoEncontradoException::new);
    }

    @Transactional(readOnly = true)
    public List<UsuarioModel> listarProfessoresParaOferta() {
        var perfis = List.of(PerfilUsuario.ROLE_RESPONSAVEL, PerfilUsuario.ROLE_SUPERVISOR);
        return usuarioRepository.findDistinctByPerfisNomeIn(perfis);
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarPorId(java.util.UUID id) {
        return usuarioRepository.findById(id).orElseThrow(RecursoNaoEncontradoException::new);
    }

    @Transactional
    public UsuarioModel criarAlunoCsv(String ra, String nomeCompleto, String email) {
        if (ra == null || ra.isBlank()) {
            throw new ValidacaoNegocioException("csv.error.ra.obrigatorio");
        }
        if (email == null || email.isBlank()) {
            throw new ValidacaoNegocioException("csv.error.email.obrigatorio");
        }
        var existentePorEmail = usuarioRepository.findByEmail(email.trim().toLowerCase());
        if (existentePorEmail.isPresent()) {
            return existentePorEmail.get();
        }
        if (usuarioRepository.findByRa(ra).isPresent()) {
            throw new ValidacaoNegocioException("csv.error.ra.duplicado");
        }

        var perfilAluno = perfilRepository.findByNome(PerfilUsuario.ROLE_ALUNO)
                .orElseThrow(RecursoNaoEncontradoException::new);

        var usuario = new UsuarioModel();
        usuario.setRa(ra.trim());
        usuario.setNomeCompleto(nomeCompleto != null ? nomeCompleto.trim() : "");
        usuario.setEmail(email.trim().toLowerCase());
        usuario.setUsername(email.trim().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(ra.trim()));
        usuario.setEnabled(true);
        usuario.adicionarPerfil(perfilAluno);

        var salvo = usuarioRepository.save(usuario);
        logger.info("Aluno criado via CSV: email={}, ra={}", salvo.getEmail(), salvo.getRa());
        return salvo;
    }
}
