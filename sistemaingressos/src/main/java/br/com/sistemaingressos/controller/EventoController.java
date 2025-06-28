package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.repository.EventoRepository;
import br.com.sistemaingressos.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // LISTAR EVENTOS
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "evento/listar";
    }

    // FORMULÁRIO PARA NOVO EVENTO
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("evento", new Evento());
        return "evento/formulario";
    }

    // SALVAR EVENTO
    @PostMapping("/salvar")
    public String salvar(@Valid Evento evento) {
        eventoService.salvar(evento);
        return "redirect:/eventos";
    }

    // EDITAR EVENTO
    @GetMapping("/editar/{id}") // ou? @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Evento evento = eventoService.buscarPorId(id);
        model.addAttribute("evento", evento);
        return "evento/formulario";
    }

    // DELETAR EVENTO
    @GetMapping("/excluir/{id}") // ou?  @GetMapping("/{id}/deletar")
    public String excluir(@PathVariable Long id) {
        eventoService.excluir(id);
        return "redirect:/eventos";
    }

}
