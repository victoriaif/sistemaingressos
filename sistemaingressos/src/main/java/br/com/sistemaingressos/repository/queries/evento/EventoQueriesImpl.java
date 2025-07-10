package br.com.sistemaingressos.repository.queries.evento;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.sistemaingressos.filter.EventoFilter;
import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.pagination.PaginacaoUtil;

@Repository
public class EventoQueriesImpl implements EventoQueries {

    @Autowired
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    public Page<Evento> pesquisar(EventoFilter filtro, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("select e from Evento e where 1=1 ");
        List<Object> params = new ArrayList<>();

        if (filtro.getId() != null) {
            jpql.append("and e.id = ?").append(params.size()+1).append(" ");
            params.add(filtro.getId());
        }
        if (filtro.getNome() != null && !filtro.getNome().isBlank()) {
            jpql.append("and lower(e.nome) like ?").append(params.size()+1).append(" ");
            params.add("%" + filtro.getNome().toLowerCase() + "%");
        }
        if (filtro.getData() != null) {
            jpql.append("and e.data = ?").append(params.size()+1).append(" ");
            params.add(filtro.getData());
        }
        if (filtro.getLocal() != null && !filtro.getLocal().isBlank()) {
            jpql.append("and lower(e.local) like ?").append(params.size()+1).append(" ");
            params.add("%" + filtro.getLocal().toLowerCase() + "%");
        }

        TypedQuery<Evento> query = manager.createQuery(jpql.toString(), Evento.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i+1, params.get(i));
        }

        // count
        String countJpql = "select count(e) " + jpql.substring(jpql.indexOf("from"));
        TypedQuery<Long> countQuery = manager.createQuery(countJpql, Long.class);
        for (int i = 0; i < params.size(); i++) {
            countQuery.setParameter(i+1, params.get(i));
        }
        long total = countQuery.getSingleResult();

        // paginação
        paginacaoUtil.preparar(query, pageable);
        List<Evento> lista = query.getResultList();

        return new PageImpl<>(lista, pageable, total);
    }
}
