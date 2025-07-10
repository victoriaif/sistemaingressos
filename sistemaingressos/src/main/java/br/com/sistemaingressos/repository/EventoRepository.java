package br.com.sistemaingressos.repository;

import br.com.sistemaingressos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

import br.com.sistemaingressos.repository.queries.evento.EventoQueries;

public interface EventoRepository extends JpaRepository<Evento, Long>, EventoQueries {

     boolean existsByNomeAndData(String nome, LocalDate data);

     List<Evento> findByNomeContainingIgnoreCase(String nome);

}