package br.com.sistemaingressos.pagination;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import jakarta.persistence.TypedQuery;

@Component
public class PaginacaoUtil {

    public void preparar(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int tamanho = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * tamanho;
        query.setFirstResult(primeiroRegistro);
        query.setMaxResults(tamanho);
    }

    public <T> Page<T> getPage(TypedQuery<T> query, Class<T> clazz, Pageable pageable) {
        List<T> resultado = query.getResultList();
        // não temos total aqui, mas se não precisar de total exato, use Slice
        return new org.springframework.data.domain.PageImpl<>(
            resultado, pageable, resultado.size()
        );
    }
}