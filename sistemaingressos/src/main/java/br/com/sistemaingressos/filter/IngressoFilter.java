package br.com.sistemaingressos.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import br.com.sistemaingressos.model.StatusIngresso;

public class IngressoFilter {

    private Long id;
    private Long eventoId;
    private String eventoNome;
    private LocalDate data;           // única data
    private String local;
    private String tipo;
    private BigDecimal preco;         // único preço
    private StatusIngresso status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getEventoNome() { return eventoNome; }
    public void setEventoNome(String eventoNome) { this.eventoNome = eventoNome; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public StatusIngresso getStatus() { return status; }
    public void setStatus(StatusIngresso status) { this.status = status; }
}
