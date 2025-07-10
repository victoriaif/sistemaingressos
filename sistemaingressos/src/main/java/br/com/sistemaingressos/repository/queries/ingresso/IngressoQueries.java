package br.com.sistemaingressos.repository.queries.ingresso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaingressos.filter.IngressoFilter;
import br.com.sistemaingressos.model.Ingresso;

public interface IngressoQueries {
    Page<Ingresso> pesquisar(IngressoFilter filtro, Pageable pageable);
}
