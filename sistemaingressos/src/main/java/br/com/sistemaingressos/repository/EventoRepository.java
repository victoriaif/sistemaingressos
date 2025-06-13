package br.com.sistemaingressos.repository;

import br.com.sistemaingressos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}