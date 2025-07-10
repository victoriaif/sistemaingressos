package br.com.sistemaingressos.repository;

import br.com.sistemaingressos.model.Transacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
        // busca todas as transações em que o usuário foi comprador
    List<Transacao> findByCompradorEmail(String email);
}