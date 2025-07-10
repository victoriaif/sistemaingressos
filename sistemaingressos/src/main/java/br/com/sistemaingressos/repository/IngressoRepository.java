package br.com.sistemaingressos.repository;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.StatusIngresso;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.queries.ingresso.IngressoQueries;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngressoRepository extends JpaRepository<Ingresso, Long>, IngressoQueries {
    List<Ingresso> findByEventoId(Long eventoId);
    List<Ingresso> findByStatus(StatusIngresso status);
    List<Ingresso> findByUsuarioAnunciante(Usuario usuario);
    Page<Ingresso> findByStatus(StatusIngresso status, Pageable pageable);
    // método para listar todos os ingressos que ESTE usuário (pelo email) anunciou
    List<Ingresso> findByUsuarioAnuncianteEmail(String email);
}
