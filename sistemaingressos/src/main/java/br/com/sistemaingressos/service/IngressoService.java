package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.StatusIngresso;
import br.com.sistemaingressos.repository.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class IngressoService {

    private final IngressoRepository ingressoRepository;

    public IngressoService(IngressoRepository ingressoRepository) {
        this.ingressoRepository = ingressoRepository;
    }

    public void salvar(Ingresso ingresso) {
        //Regra de negócio: o preço nao pode ser <= 0 
        if (ingresso.getPreco() == null || ingresso.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("O preço do ingresso deve ser maior que zero.");
        }

         // Regra: o preço deve ter exatamente 2 casas decimais
        if (ingresso.getPreco().scale() != 2) {
            throw new IllegalArgumentException("O preço deve conter exatamente duas casas decimais (ex: 10.00).");
        }

        //Regra de negócio: o ingresso já é setado como disponível
        ingresso.setStatus(StatusIngresso.DISPONIVEL);
        
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
