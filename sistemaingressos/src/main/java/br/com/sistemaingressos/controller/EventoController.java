package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.repository.EventoRepository;
import br.com.sistemaingressos.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public String salvar(@Valid Evento evento, org.springframework.validation.BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "evento/formulario";
        }

        try {
            eventoService.salvar(evento);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            return "evento/formulario";
        }

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
    @GetMapping("/excluir/{id}") // ou? @GetMapping("/{id}/deletar")
    public String excluir(@PathVariable Long id) {
        eventoService.excluir(id);
        return "redirect:/eventos";
    }

    //Regre de negócio - Ingresso: puxa data e local do evento selecionado
    @GetMapping("/detalhes/{id}")
    @ResponseBody
    public Evento buscarDetalhesEvento(@PathVariable Long id) {
        return eventoService.buscarPorId(id);
    }
    //ou 
    // @GetMapping("/detalhes/{id}")
    // @ResponseBody
    // public ResponseEntity<Evento> detalhes(@PathVariable Long id) {
    //     Evento evento = eventoService.buscarPorId(id);
    //     return ResponseEntity.ok(evento);
    // }


}
