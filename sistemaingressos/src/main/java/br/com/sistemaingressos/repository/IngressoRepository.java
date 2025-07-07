package br.com.sistemaingressos.repository;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.StatusIngresso;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
    List<Ingresso> findByEventoId(Long eventoId);
    List<Ingresso> findByStatus(StatusIngresso status);
}
