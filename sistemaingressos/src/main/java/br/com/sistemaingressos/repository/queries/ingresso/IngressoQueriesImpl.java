package br.com.sistemaingressos.repository.queries.ingresso;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.sistemaingressos.filter.IngressoFilter;
import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.pagination.PaginacaoUtil;
import org.springframework.data.domain.PageImpl;

@Repository
public class IngressoQueriesImpl implements IngressoQueries {

    @Autowired
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    public Page<Ingresso> pesquisar(IngressoFilter filtro, Pageable pageable) {
        var jpql = new StringBuilder("select i from Ingresso i join fetch i.evento e where 1=1 ");
        var params = new ArrayList<Object>();

        if (filtro.getId() != null) {
            jpql.append("and i.id = ?").append(params.size() + 1).append(" ");
            params.add(filtro.getId());
        }
        if (filtro.getEventoId() != null) {
            jpql.append("and e.id = ?").append(params.size() + 1).append(" ");
            params.add(filtro.getEventoId());
        }
        if (filtro.getEventoNome() != null && !filtro.getEventoNome().isBlank()) {
            jpql.append("and lower(e.nome) like ?").append(params.size() + 1).append(" ");
            params.add("%" + filtro.getEventoNome().toLowerCase() + "%");
        }

        if (filtro.getLocal() != null && !filtro.getLocal().isBlank()) {
            jpql.append("and lower(i.local) like ?").append(params.size() + 1).append(" ");
            params.add("%" + filtro.getLocal().toLowerCase() + "%");
        }
        if (filtro.getTipo() != null && !filtro.getTipo().isBlank()) {
            jpql.append("and lower(i.tipo) like ?").append(params.size() + 1).append(" ");
            params.add("%" + filtro.getTipo().toLowerCase() + "%");
        }

        if (filtro.getStatus() != null) {
            jpql.append("and i.status = ?").append(params.size() + 1).append(" ");
            params.add(filtro.getStatus());
        }

        if (filtro.getData() != null) {
            jpql.append("and i.data = ?").append(params.size() + 1).append(" ");
            params.add(filtro.getData());
        }

        if (filtro.getPreco() != null) {
            jpql.append("and i.preco = ?").append(params.size() + 1).append(" ");
            params.add(filtro.getPreco());
        }

        TypedQuery<Ingresso> query = manager.createQuery(jpql.toString(), Ingresso.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }

        // total de resultados
        TypedQuery<Long> countQuery = manager.createQuery(
                "select count(i) from Ingresso i join i.evento e where " +
                        jpql.substring(jpql.indexOf("where") + 6),
                Long.class);
        for (int i = 0; i < params.size(); i++) {
            countQuery.setParameter(i + 1, params.get(i));
        }
        long total = countQuery.getSingleResult();

        // aplica paginação
        paginacaoUtil.preparar(query, pageable);
        List<Ingresso> lista = query.getResultList();

        return new PageImpl<>(lista, pageable, total);
    }
}
