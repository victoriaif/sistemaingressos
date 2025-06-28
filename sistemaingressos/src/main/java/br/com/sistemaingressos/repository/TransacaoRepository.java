package br.com.sistemaingressos.repository;

import br.com.sistemaingressos.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    
}