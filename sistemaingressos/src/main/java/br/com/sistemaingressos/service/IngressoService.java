package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.repository.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IngressoService {

    private final IngressoRepository ingressoRepository;

    public IngressoService(IngressoRepository ingressoRepository) {
        this.ingressoRepository = ingressoRepository;
    }

    public void salvar(Ingresso ingresso) {
        ingressoRepository.save(ingresso);
    }

    public void alterar(Ingresso ingresso) {
        ingressoRepository.save(ingresso);
    }

    public void excluir(Long id) {
        if (!ingressoRepository.existsById(id)) {
            throw new RuntimeException("Ingresso com ID inválido");
        }
        ingressoRepository.deleteById(id);
    }

    public Ingresso buscarPorId(Long id) {
        return ingressoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ingresso com ID inválido"));
    }

    public List<Ingresso> listarTodos() {
        return ingressoRepository.findAll();
    }
}
