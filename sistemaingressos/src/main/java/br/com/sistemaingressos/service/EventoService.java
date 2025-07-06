package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.repository.EventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;
    //Listar ingressos por evento
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public void salvar(Evento evento) {
        boolean duplicado = eventoRepository.existsByNomeAndData(evento.getNome(), evento.getData());

        // Regra de negócio: data não pode ser no passado
        if (evento.getData() != null && evento.getData().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("A data do evento não pode ser no passado.");
        }

        // Regra de negócio: evento não pode ser duplicado (mesmo nome e mesma data)
        // Se for novo (sem ID ainda) e duplicado, rejeita
        if (evento.getId() == null && duplicado) {
            throw new IllegalArgumentException("Já existe um evento com esse nome e data.");
        }

        eventoRepository.save(evento);
    }


    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    public List<Evento> buscarPorNome(String nome) {
    return eventoRepository.findByNomeContainingIgnoreCase(nome);
}


    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Evento não encontrado com ID: " + id)
        );
    }

    public void excluir(Long id) {
        eventoRepository.deleteById(id);
    }
}
