package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.repository.EventoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/eventos")
public class EventoController {

      @Autowired
    private EventoRepository eventoRepository;

    // LISTAR EVENTOS
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("eventos", eventoRepository.findAll());
        return "evento/listar";
    }

    // FORMULÁRIO PARA NOVO EVENTO
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("evento", new Evento());
        return "evento/formulario";
    }

    // SALVAR EVENTO
    @PostMapping
    public String salvar(@Valid Evento evento) {
        eventoRepository.save(evento);
        return "redirect:/eventos";
    }

    // EDITAR EVENTO
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Evento evento = eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento inválido: " + id));
        model.addAttribute("evento", evento);
        return "evento/formulario";
    }

    // DELETAR EVENTO
    @GetMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id) {
        eventoRepository.deleteById(id);
        return "redirect:/eventos";
    }

}
