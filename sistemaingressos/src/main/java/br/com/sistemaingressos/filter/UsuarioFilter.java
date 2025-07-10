package br.com.sistemaingressos.filter;

import java.time.LocalDate;

public class UsuarioFilter {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
    private String papel;    // nome do papel
    private Boolean ativo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
