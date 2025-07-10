package br.com.sistemaingressos.repository.queries.usuario;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import br.com.sistemaingressos.filter.UsuarioFilter;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.pagination.PaginacaoUtil;

@Repository
public class UsuarioQueriesImpl implements UsuarioQueries {

    @Autowired
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    public Page<Usuario> pesquisar(UsuarioFilter filtro, Pageable pageable) {
        var jpql = new StringBuilder("select u from Usuario u ");
        var params = new ArrayList<Object>();

        // Join só se for filtrar papel
        if (filtro.getPapel() != null && !filtro.getPapel().isBlank()) {
            jpql.append(" join u.papeis p ");
        }

        jpql.append(" where 1=1 ");

        if (filtro.getId() != null) {
            jpql.append(" and u.id = ?").append(params.size()+1);
            params.add(filtro.getId());
        }
        if (filtro.getNome() != null && !filtro.getNome().isBlank()) {
            jpql.append(" and lower(u.nome) like ?").append(params.size()+1);
            params.add("%"+filtro.getNome().toLowerCase()+"%");
        }
        if (filtro.getCpf() != null && !filtro.getCpf().isBlank()) {
            jpql.append(" and u.cpf like ?").append(params.size()+1);
            params.add("%"+filtro.getCpf()+"%");
        }
        if (filtro.getEmail() != null && !filtro.getEmail().isBlank()) {
            jpql.append(" and lower(u.email) like ?").append(params.size()+1);
            params.add("%"+filtro.getEmail().toLowerCase()+"%");
        }
        if (filtro.getDataNascimento() != null) {
            jpql.append(" and u.dataNascimento = ?").append(params.size()+1);
            params.add(filtro.getDataNascimento());
        }
        if (filtro.getPapel() != null && !filtro.getPapel().isBlank()) {
            jpql.append(" and lower(p.nome) like ?").append(params.size()+1);
            params.add("%"+filtro.getPapel().toLowerCase()+"%");
        }
        if (filtro.getAtivo() != null) {
            jpql.append(" and u.ativo = ?").append(params.size()+1);
            params.add(filtro.getAtivo());
        }

        // Cria query de dados
        TypedQuery<Usuario> query = manager.createQuery(jpql.toString(), Usuario.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i+1, params.get(i));
        }

        // Cria query de contagem
        String countJpql = "select count(u) " + jpql.substring(jpql.indexOf("from"));
        TypedQuery<Long> countQuery = manager.createQuery(countJpql, Long.class);
        for (int i = 0; i < params.size(); i++) {
            countQuery.setParameter(i+1, params.get(i));
        }
        long total = countQuery.getSingleResult();

        // paginação
        paginacaoUtil.preparar(query, pageable);
        List<Usuario> lista = query.getResultList();

        return new PageImpl<>(lista, pageable, total);
    }
}
