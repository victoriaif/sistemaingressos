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
import java.util.List;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    //LISTAR EVENTOS COM FILTRO OPCIONAL (barra de pesquisa)
    @GetMapping
    public String listar(@RequestParam(name = "q", required = false) String query, Model model) {
        List<Evento> eventos;
        if (query != null && !query.isEmpty()) {
            eventos = eventoService.buscarPorNome(query);
        } else {
            eventos = eventoService.listarTodos();
        }
        model.addAttribute("eventos", eventos);
        model.addAttribute("query", query);
        return "evento/listar";
    }

    // AUTOCOMPLETE JSON (busca rápida para a lista suspensa)
    @GetMapping("/autocomplete")
    @ResponseBody
    public List<Evento> buscarPorNomeJson(@RequestParam("q") String query) {
        return eventoService.buscarPorNome(query);
    }

    //FORMULÁRIO PARA NOVO EVENTO
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("evento", new Evento());
        return "evento/formulario";
    }

    //SALVAR EVENTO
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

    //EDITAR EVENTO
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Evento evento = eventoService.buscarPorId(id);
        model.addAttribute("evento", evento);
        return "evento/formulario";
    }

    //EXCLUIR EVENTO
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        eventoService.excluir(id);
        return "redirect:/eventos";
    }

    //BUSCAR DETALHES DO EVENTO (JSON)
    @GetMapping("/detalhes/{id}")
    @ResponseBody
    public Evento buscarDetalhesEvento(@PathVariable Long id) {
        return eventoService.buscarPorId(id);
    }

    @GetMapping("/publicos")
    public String listarPublico(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "evento/listar-publico";
    }
}

