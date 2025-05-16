package br.com.sistemaingressos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @NotNull
    private LocalDate dataDoEvento;

    @NotBlank
    private String local;

    @NotNull
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private StatusIngresso status;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioAnunciante;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LocalDate getDataDoEvento() {
        return dataDoEvento;
    }

    public void setDataDoEvento(LocalDate dataDoEvento) {
        this.dataDoEvento = dataDoEvento;
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

    public Usuario getUsuarioAnunciante() {
        return usuarioAnunciante;
    }

    public void setUsuarioAnunciante(Usuario usuarioAnunciante) {
        this.usuarioAnunciante = usuarioAnunciante;
    }
}


/*
RASCUNHO METODOS

    public void disponibilizarParaVenda() {
        this.status = StatusIngresso.DISPONIVEL;
    }

    public void atualizarStatus(StatusIngresso novoStatus) {
        this.status = novoStatus;
    }

    public boolean ehValidoParaVenda() {
        return status == StatusIngresso.DISPONIVEL || status == StatusIngresso.REVENDA;
    }

}

 */
