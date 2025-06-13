package br.com.sistemaingressos.repository;

import br.com.sistemaingressos.model.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
    
}
