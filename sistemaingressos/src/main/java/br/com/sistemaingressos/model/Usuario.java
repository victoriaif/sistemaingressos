package br.com.sistemaingressos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
// import java.util.List;
// import java.time.LocalDate;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String cpf;

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    // 🔗 Se você quiser, pode manter apenas esta relação de dono de ingresso
    @OneToMany(mappedBy = "usuarioAnunciante")
    private java.util.List<Ingresso> ingressos;

    // 🔧 Construtor vazio
    public Usuario() {
    }

    // 🔧 Construtor com campos principais
    public Usuario(String cpf, String nome, String email, String senha, TipoUsuario tipoUsuario) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    // ✅ Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public java.util.List<Ingresso> getIngressos() {
        return ingressos;
    }

    public void setIngressos(java.util.List<Ingresso> ingressos) {
        this.ingressos = ingressos;
    }
}
