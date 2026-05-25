package br.ufscar.dc.dsw.PESCD.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_USUARIO")
public class UsuarioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nomeUsuario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "professorResponsavel")
    private Set<OfertaModel> ofertasResponsavel = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "aluno")
    private Set<AlunoOfertaModel> matriculas = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "professorSupervisor")
    private Set<PlanoTrabalhoModel> planosSupervisionados = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public Set<OfertaModel> getOfertasResponsavel() {
        return ofertasResponsavel;
    }

    public void setOfertasResponsavel(Set<OfertaModel> ofertasResponsavel) {
        this.ofertasResponsavel = ofertasResponsavel;
    }

    public Set<AlunoOfertaModel> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(Set<AlunoOfertaModel> matriculas) {
        this.matriculas = matriculas;
    }

    public Set<PlanoTrabalhoModel> getPlanosSupervisionados() {
        return planosSupervisionados;
    }

    public void setPlanosSupervisionados(Set<PlanoTrabalhoModel> planosSupervisionados) {
        this.planosSupervisionados = planosSupervisionados;
    }
}
