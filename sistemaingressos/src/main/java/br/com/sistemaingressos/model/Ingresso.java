package br.com.sistemaingressos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Ingresso { // ou public class Ingresso<Usuario> { ??

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String tipo;

    @NotNull
    private LocalDate data;

    @NotBlank
    private String local;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private StatusIngresso status;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioAnunciante;

    @OneToOne(mappedBy = "ingresso", cascade = CascadeType.ALL)
    private Transacao transacao;

    // 🔧 Construtor vazio (JPA)
    public Ingresso() {
    }

    // 🔧 Construtor com campos principais (sem evento/usuario/transação)
    public Ingresso(LocalDate data, String local, BigDecimal preco, StatusIngresso status) {
        this.data = data;
        this.local = local;
        this.preco = preco;
        this.status = status;
    }

    // ✅ Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public StatusIngresso getStatus() {
        return status;
    }

    public void setStatus(StatusIngresso status) {
        this.status = status;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getUsuarioAnunciante() {
        return usuarioAnunciante;
    }

    public void setUsuarioAnunciante(Usuario usuarioAnunciante) {
        this.usuarioAnunciante = usuarioAnunciante;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }
}

/*
 * RASCUNHO METODOS
 * 
 * public void disponibilizarParaVenda() {
 * this.status = StatusIngresso.DISPONIVEL;
 * }
 * 
 * public void atualizarStatus(StatusIngresso novoStatus) {
 * this.status = novoStatus;
 * }
 * 
 * public boolean ehValidoParaVenda() {
 * return status == StatusIngresso.DISPONIVEL || status ==
 * StatusIngresso.REVENDA;
 * }
 * 
 * }
 * 
 */
