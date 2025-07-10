package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.StatusIngresso;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.IngressoRepository;
import br.com.sistemaingressos.service.IngressoService;
import br.com.sistemaingressos.service.UsuarioService;
import br.com.sistemaingressos.service.EventoService;
import br.com.sistemaingressos.repository.EventoRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/ingressos")
public class IngressoController {

    @Autowired
    // private IngressoRepository ingressoRepository;
    private IngressoService ingressoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventoService eventoService;

    // LISTAR todos os ingressos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ingressos", ingressoService.listarTodos());
        return "ingresso/listar";
    }

    // Página para escolher se o ingresso é compra ou venda
    @GetMapping("/anunciar")
    public String anunciar() {
        return "ingresso/anunciar";
    }

    // Formulário para novo ingresso
    @GetMapping("/novo")
    public String novo(Model model) {
        Ingresso ingresso = new Ingresso();
        ingresso.setStatus(StatusIngresso.DISPONIVEL); // Aqui seta o valor inicial
        model.addAttribute("ingresso", ingresso);
        model.addAttribute("eventos", eventoRepository.findAll());
        return "ingresso/formulario";
    }

    // Listar ingressos por evento
    @GetMapping("/evento/{idEvento}")
    public String listarPorEvento(@PathVariable Long idEvento, Model model) {
        List<Ingresso> ingressos = ingressoService.listarPorEvento(idEvento);
        Evento evento = eventoService.buscarPorId(idEvento);

        model.addAttribute("ingressos", ingressos);
        model.addAttribute("evento", evento);
        return "ingresso/listar-por-evento";
    }

    // SALVAR novo ingresso ou atualizar existente
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("ingresso") Ingresso ingresso, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("eventos", eventoRepository.findAll());
            return "ingresso/formulario";
        }

        try {
            //Se o ID é NULO, é um NOVO ingresso
            if (ingresso.getId() == null) {
                Usuario usuarioLogado = usuarioService.getUsuarioLogado();
                ingresso.setUsuarioAnunciante(usuarioLogado);
                ingresso.setStatus(StatusIngresso.DISPONIVEL);
            }
            //Se o ID EXISTE, é uma EDIÇÃO.
            else {
                
                Ingresso ingressoExistente = ingressoService.buscarPorId(ingresso.getId());
                ingresso.setUsuarioAnunciante(ingressoExistente.getUsuarioAnunciante());
            }

            ingressoService.salvar(ingresso);

        } catch (Exception ex) {
            model.addAttribute("erro", ex.getMessage());
            model.addAttribute("eventos", eventoRepository.findAll());
            return "ingresso/formulario";
        }
        
        //Redireciona para a lista de ingressos do usuário após salvar
        return "redirect:/ingressos/vender";
    }

    // EDITAR ingresso existente
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Ingresso ingresso = ingressoService.buscarPorId(id);
        model.addAttribute("ingresso", ingresso);
        model.addAttribute("eventos", eventoRepository.findAll());
        return "ingresso/formulario";
    }

    // EXCLUIR ingresso
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        ingressoService.excluir(id);
        return "redirect:/ingressos";
    }

    // Comprar - LISTA DE INGRESSOS PAGINADA
    @GetMapping("/comprar")
    public String listarIngressosParaCompra(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ingresso> ingressosPage = ingressoService.listarPorStatusPaginado(StatusIngresso.DISPONIVEL, pageable);
        model.addAttribute("ingressosPage", ingressosPage);
        return "ingresso/comprar";
    }

    // Listar ingressos do vendedor
    @GetMapping("/vender")
    public String meusAnuncios(Model model) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        List<Ingresso> meusIngressos = ingressoService.listarPorUsuario(usuario);

        model.addAttribute("ingressos", meusIngressos);
        return "ingresso/vender";
    }

}
