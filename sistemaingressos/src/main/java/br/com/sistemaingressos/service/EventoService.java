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

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public void salvar(Evento evento) {
        eventoRepository.save(evento);
    }

    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
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
