package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.filter.EventoFilter;
import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.pagination.PageWrapper;
import br.com.sistemaingressos.repository.EventoRepository;
import br.com.sistemaingressos.service.EventoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
private EventoRepository eventoRepository;

    //LISTAR EVENTOS COM FILTRO OPCIONAL (barra de pesquisa)
    // @GetMapping
    // public String listar(@RequestParam(name = "q", required = false) String query, Model model) {
    //     List<Evento> eventos;
    //     if (query != null && !query.isEmpty()) {
    //         eventos = eventoService.buscarPorNome(query);
    //     } else {
    //         eventos = eventoService.listarTodos();
    //     }
    //     model.addAttribute("eventos", eventos);
    //     model.addAttribute("query", query);
    //     return "evento/listar";
    // }


   @GetMapping
    public String listar(
        EventoFilter filtro,
        @PageableDefault(size = 10)
        @SortDefault(sort = "id", direction = Sort.Direction.ASC)
        Pageable pageable,
        HttpServletRequest request,
        Model model
    ) {
        Page<Evento> pagina = eventoRepository.pesquisar(filtro, pageable);
       model.addAttribute("pagina", new PageWrapper<>(pagina, request));
        model.addAttribute("filtro", filtro);

        // configura ordenação para o template
        Sort.Order order = pageable.getSort().iterator().hasNext()
            ? pageable.getSort().iterator().next()
            : Sort.Order.asc("id");
        model.addAttribute("sortField", order.getProperty());
        model.addAttribute("sortDir",   order.isAscending() ? "asc" : "desc");
        model.addAttribute("reverseDir", order.isAscending() ? "desc" : "asc");

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

    // @GetMapping("/publicos")
    // public String listarPublico(Model model) {
    //     model.addAttribute("eventos", eventoService.listarTodos());
    //     return "evento/listar-publico";
    // }

    @GetMapping("/publicos")
public String listarPublico(@RequestParam(name="q", required=false) String query,
                            Model model) {
    List<Evento> eventos = (query != null && !query.isBlank())
        ? eventoService.buscarPorNome(query)
        : eventoService.listarTodos();
    model.addAttribute("eventos", eventos);
    model.addAttribute("query",   query);
    return "evento/listar-publico";
}



    
}

