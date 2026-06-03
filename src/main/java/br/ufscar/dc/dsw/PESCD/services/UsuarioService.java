package br.ufscar.dc.dsw.PESCD.services;

import br.ufscar.dc.dsw.PESCD.dtos.AdminUsuarioForm;
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

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

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
    public List<UsuarioModel> listarSupervisores() {
        return usuarioRepository.findByPerfisNome(PerfilUsuario.ROLE_SUPERVISOR);
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarPorId(java.util.UUID id) {
        return usuarioRepository.findById(id).orElseThrow(RecursoNaoEncontradoException::new);
    }

    @Transactional(readOnly = true)
    public List<UsuarioModel> listarUsuariosAdmin() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PerfilUsuario> listarPerfisGerenciaveis() {
        return List.of(
                PerfilUsuario.ROLE_ADMIN,
                PerfilUsuario.ROLE_SECRETARIO,
                PerfilUsuario.ROLE_SUPERVISOR,
                PerfilUsuario.ROLE_RESPONSAVEL
        );
    }

    @Transactional(readOnly = true)
    public AdminUsuarioForm prepararForm(UUID id) {
        var usuario = buscarPorId(id);
        var form = new AdminUsuarioForm();
        form.setNomeCompleto(usuario.getNomeCompleto());
        form.setEmail(usuario.getEmail());
        form.setUsername(usuario.getUsername());
        form.setEnabled(usuario.isEnabled());
        usuario.getPerfis().stream()
                .map(perfil -> perfil.getNome())
                .filter(this::isPerfilGerenciavel)
                .findFirst()
                .ifPresent(form::setPerfil);
        return form;
    }

    @Transactional
    public UsuarioModel criarUsuarioAdmin(AdminUsuarioForm form) {
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new ValidacaoNegocioException("admin.usuario.error.password.obrigatoria");
        }
        validarSenha(form.getPassword());
        validarPerfilGerenciavel(form.getPerfil());
        validarEmailUnico(form.getEmail(), null);
        validarUsernameUnico(form.getUsername(), null);

        var usuario = new UsuarioModel();
        aplicarDadosBasicos(usuario, form);
        usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        atribuirPerfil(usuario, form.getPerfil());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioModel atualizarUsuarioAdmin(UUID id, AdminUsuarioForm form) {
        validarPerfilGerenciavel(form.getPerfil());
        var usuario = buscarPorId(id);
        validarEmailUnico(form.getEmail(), id);
        validarUsernameUnico(form.getUsername(), id);

        aplicarDadosBasicos(usuario, form);
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            validarSenha(form.getPassword());
            usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        }
        atribuirPerfil(usuario, form.getPerfil());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluirUsuarioAdmin(UUID id, String usernameLogado) {
        var usuario = buscarPorId(id);
        if (usuario.getUsername().equals(usernameLogado)) {
            throw new ValidacaoNegocioException("admin.usuario.error.autoexclusao");
        }
        usuarioRepository.delete(usuario);
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

    private void aplicarDadosBasicos(UsuarioModel usuario, AdminUsuarioForm form) {
        usuario.setNomeCompleto(form.getNomeCompleto().trim());
        usuario.setEmail(form.getEmail().trim().toLowerCase());
        usuario.setUsername(form.getUsername().trim());
        usuario.setEnabled(form.isEnabled());
    }

    private void atribuirPerfil(UsuarioModel usuario, PerfilUsuario perfilUsuario) {
        var perfil = perfilRepository.findByNome(perfilUsuario)
                .orElseThrow(RecursoNaoEncontradoException::new);
        usuario.getPerfis().clear();
        usuario.adicionarPerfil(perfil);
    }

    private void validarEmailUnico(String email, UUID usuarioIdAtual) {
        usuarioRepository.findByEmail(email.trim().toLowerCase()).ifPresent(usuario -> {
            if (usuarioIdAtual == null || !usuario.getId().equals(usuarioIdAtual)) {
                throw new ValidacaoNegocioException("admin.usuario.error.email.duplicado");
            }
        });
    }

    private void validarUsernameUnico(String username, UUID usuarioIdAtual) {
        usuarioRepository.findByUsername(username.trim()).ifPresent(usuario -> {
            if (usuarioIdAtual == null || !usuario.getId().equals(usuarioIdAtual)) {
                throw new ValidacaoNegocioException("admin.usuario.error.username.duplicado");
            }
        });
    }

    private void validarPerfilGerenciavel(PerfilUsuario perfil) {
        if (!isPerfilGerenciavel(perfil)) {
            throw new ValidacaoNegocioException("admin.usuario.error.perfil.invalido");
        }
    }

    private void validarSenha(String password) {
        if (password.length() < 6) {
            throw new ValidacaoNegocioException("admin.usuario.error.password.tamanho");
        }
    }

    private boolean isPerfilGerenciavel(PerfilUsuario perfil) {
        return EnumSet.of(
                PerfilUsuario.ROLE_ADMIN,
                PerfilUsuario.ROLE_SECRETARIO,
                PerfilUsuario.ROLE_SUPERVISOR,
                PerfilUsuario.ROLE_RESPONSAVEL
        ).contains(perfil);
    }
}
