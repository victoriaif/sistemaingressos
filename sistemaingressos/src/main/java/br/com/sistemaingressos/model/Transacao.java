package br.com.sistemaingressos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dataHora;

    @NotNull
    private BigDecimal valor;

    @OneToOne
    @JoinColumn(name = "ingresso_id")
    private Ingresso ingresso;

    @ManyToOne
    @JoinColumn(name = "comprador_id")
    private Usuario comprador;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    // 🔧 Construtor vazio
    public Transacao() {
    }

    // 🔧 Construtor com campos principais
    public Transacao(LocalDateTime dataHora, BigDecimal valor, Ingresso ingresso, Usuario comprador, Usuario vendedor) {
        this.dataHora = dataHora;
        this.valor = valor;
        this.ingresso = ingresso;
        this.comprador = comprador;
        this.vendedor = vendedor;
    }

    // ✅ Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Ingresso getIngresso() {
        return ingresso;
    }

    public void setIngresso(Ingresso ingresso) {
        this.ingresso = ingresso;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }
}


/*
RASCUNHO METODOS

    public void registrarTransacao() {
        // lógica para registrar no sistema
    }

    public boolean validarPagamento() {
        return valor != null && valor.compareTo(BigDecimal.ZERO) > 0;
    }

    // Getters e setters
}

 */