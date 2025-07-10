package br.com.sistemaingressos.repository.queries.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaingressos.filter.UsuarioFilter;
import br.com.sistemaingressos.model.Usuario;

public interface UsuarioQueries {
    Page<Usuario> pesquisar(UsuarioFilter filtro, Pageable pageable);
}
