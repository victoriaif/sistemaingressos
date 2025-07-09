package br.com.sistemaingressos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
// import java.util.List;
// import java.time.LocalDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 000.000.000-00")
    @NotBlank
    private String cpf;

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_papel", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "papel_id"))
    private Set<Papel> papeis = new HashSet<>();

    // 🔗 Se você quiser, pode manter apenas esta relação de dono de ingresso
    @OneToMany(mappedBy = "usuarioAnunciante")
    private java.util.List<Ingresso> ingressos;

    // 🔧 Construtor vazio
    public Usuario() {
    }

    // 🔧 Construtor com campos principais
    public Usuario(String cpf, String nome, String email, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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

    public Set<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(Set<Papel> papeis) {
        this.papeis = papeis;
    }

    public java.util.List<Ingresso> getIngressos() {
        return ingressos;
    }

    public void setIngressos(java.util.List<Ingresso> ingressos) {
        this.ingressos = ingressos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
