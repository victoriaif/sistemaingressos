package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Transacao;
import br.com.sistemaingressos.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public void salvar(Transacao transacao) {
        transacaoRepository.save(transacao);
    }

    public void alterar(Transacao transacao) {
        transacaoRepository.save(transacao);
    }

    public void excluir(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new RuntimeException("Transação com ID inválido");
        }
        transacaoRepository.deleteById(id);
    }

    public Transacao buscarPorId(Long id) {
        return transacaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transação com ID inválido"));
    }

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }
}
