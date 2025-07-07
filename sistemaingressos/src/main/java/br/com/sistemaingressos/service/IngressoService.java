package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.StatusIngresso;
import br.com.sistemaingressos.repository.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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

         // Regra de negocio: o preço deve ter exatamente 2 casas decimais
        if (ingresso.getPreco().scale() != 2) {
            throw new IllegalArgumentException("O preço deve conter exatamente duas casas decimais (ex: 10.00).");
        }

        //Regra de negocio: a data do evento vinculado nao pode ser anterior à 'hoje'
        if (ingresso.getData() == null || ingresso.getData().isBefore(LocalDate.now())) {
         throw new IllegalArgumentException("A data deste evento já passou. Não é possível anunciar o ingresso.");
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

    //Regra de negócio: atualiza Status pra 'expirado' quando a data do Evento estiver no passado
    public Ingresso buscarPorId(Long id) {
        Ingresso ingresso = ingressoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ingresso com ID inválido"));

        // Atualiza status se necessário
        if (ingresso.getData().isBefore(LocalDate.now()) && ingresso.getStatus() != StatusIngresso.EXPIRADO) {
            ingresso.setStatus(StatusIngresso.EXPIRADO);
            ingressoRepository.save(ingresso);
        }

        return ingresso;
    }

    //Listar ingressos por evento
    public List<Ingresso> listarPorEvento(Long idEvento) {
    return ingressoRepository.findByEventoId(idEvento);
}

    public List<Ingresso> listarTodos() {
    List<Ingresso> ingressos = ingressoRepository.findAll();
    atualizarIngressosExpirados(ingressos);
    return ingressos;
}

    //Listar ingresso por status
    public List<Ingresso> listarPorStatus(StatusIngresso status) {
    return ingressoRepository.findByStatus(status);
}

    //Regra de negócio: atualiza Status pra 'expirado' quando a data do Evento estiver no passado
    private void atualizarIngressosExpirados(List<Ingresso> ingressos) {
    LocalDate hoje = LocalDate.now();
    for (Ingresso ingresso : ingressos) {
        if (ingresso.getData().isBefore(hoje) && ingresso.getStatus() != StatusIngresso.EXPIRADO) {
            ingresso.setStatus(StatusIngresso.EXPIRADO);
            ingressoRepository.save(ingresso);
        }
    }
}


}
