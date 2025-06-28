package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.repository.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public void remover(Long id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);
        if (ingressoOptional.isPresent()) {
            ingressoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Ingresso com ID inválido");
        }
    }

    public Optional<Ingresso> buscarPorId(Long id) {
        return ingressoRepository.findById(id);
    }
}