package br.com.sistemaingressos.repository;

import br.com.sistemaingressos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

     boolean existsByNomeAndData(String nome, LocalDate data);

     List<Evento> findByNomeContainingIgnoreCase(String nome);

}