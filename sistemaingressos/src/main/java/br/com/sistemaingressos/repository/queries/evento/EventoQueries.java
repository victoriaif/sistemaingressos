package br.com.sistemaingressos.repository.queries.evento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaingressos.filter.EventoFilter;
import br.com.sistemaingressos.model.Evento;

public interface EventoQueries {
    Page<Evento> pesquisar(EventoFilter filtro, Pageable pageable);
}

