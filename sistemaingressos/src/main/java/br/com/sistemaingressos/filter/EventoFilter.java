package br.com.sistemaingressos.filter;

import java.time.LocalDate;

public class EventoFilter {

    private Long id;
    private String nome;
    private LocalDate data;
    private String local;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
}
