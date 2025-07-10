package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.pagination.PageWrapper;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.servlet.http.HttpServletRequest;
import br.com.sistemaingressos.filter.IngressoFilter;
import org.springframework.data.domain.Page;

import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.StatusIngresso;
import br.com.sistemaingressos.model.Transacao;
import br.com.sistemaingressos.model.Usuario;

import br.com.sistemaingressos.repository.IngressoRepository;
import br.com.sistemaingressos.service.IngressoService;
import br.com.sistemaingressos.service.UsuarioService;
import br.com.sistemaingressos.service.EventoService;
import br.com.sistemaingressos.repository.EventoRepository;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import br.com.sistemaingressos.repository.TransacaoRepository;



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

    @Autowired
    private IngressoRepository ingressoRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public IngressoController(IngressoRepository ingressoRepository) {
        this.ingressoRepository = ingressoRepository;
    }

    // LISTAR todos os ingressos
    // @GetMapping
    // public String listar(Model model) {
    //     model.addAttribute("ingressos", ingressoService.listarTodos());
    //     return "ingresso/listar";
    // }

//     @GetMapping
// public String listar(
//     IngressoFilter filtro,
//     @PageableDefault(size = 10)
//     @SortDefault(sort = "id", direction = Sort.Direction.ASC)
//     Pageable pageable,
//     HttpServletRequest request,
//     Model model
// ) {
//     // chama o custom query (ou findAll) com filtros/paginação
//     Page<Ingresso> pagina = ingressoRepository.pesquisar(filtro, pageable);

//     model.addAttribute("pagina", new PageWrapper<>(pagina, request));
//     model.addAttribute("filtro", filtro);
//     model.addAttribute("sortField", pageable.getSort().toString());
//     model.addAttribute("sortDir", pageable.getSort().isSorted() ? "asc" : "desc");
//     model.addAttribute("reverseDir", pageable.getSort().isSorted() && pageable.getSort().iterator().next().isAscending() ? "desc" : "asc");

//     return "ingresso/listar";
// }


@GetMapping
public String listar(
    IngressoFilter filtro,
    @PageableDefault(size = 10)
    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
    Pageable pageable,
    HttpServletRequest request,
    Model model
) {
    Page<Ingresso> pagina = ingressoRepository.pesquisar(filtro, pageable);

    model.addAttribute("pagina", new PageWrapper<>(pagina, request));
    model.addAttribute("filtro", filtro);

    Sort.Order order = pageable.getSort().iterator().hasNext()
                      ? pageable.getSort().iterator().next()
                      : Sort.Order.asc("id");
    String sortField = order.getProperty();
    String sortDir   = order.isAscending() ? "asc" : "desc";

    model.addAttribute("sortField", sortField);
    model.addAttribute("sortDir",   sortDir);
    model.addAttribute("reverseDir", sortDir.equals("asc") ? "desc" : "asc");

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
    public String salvar(@Valid Ingresso ingresso, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("eventos", eventoRepository.findAll());
            return "ingresso/formulario";
        }

        try {
            // Regra de negócio: SETA o usuário logado como anunciante
            Usuario usuarioLogado = usuarioService.getUsuarioLogado();
            ingresso.setUsuarioAnunciante(usuarioLogado);

            ingressoService.salvar(ingresso);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            model.addAttribute("eventos", eventoRepository.findAll());
            return "ingresso/formulario";
        }

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

    /** Ingressos que você anunciou (mesmo se não vendidos) */
    @GetMapping("/anunciados")
    public String listarAnunciados(Model model, Authentication auth) {
        String email = auth.getName();
        List<Ingresso> anunciados = ingressoRepository
                .findByUsuarioAnuncianteEmail(email);
        model.addAttribute("ingressos", anunciados);
        return "ingresso/anunciados";
    }

    /** Ingressos que você comprou (filtra pelas transações) */
    // @GetMapping("/comprados")
    // public String listarComprados(Model model, Authentication auth) {
    // String email = auth.getName();
    // List<Ingresso> comprados = transacaoRepository
    // .findByCompradorEmail(email)
    // .stream()
    // .map(Transacao::getIngresso)
    // .collect(Collectors.toList());
    // model.addAttribute("ingressos", comprados);
    // return "ingresso/comprados";
    // }

    @GetMapping("/comprados")
    public String listarComprados(Model model, Authentication auth) {
        String email = auth.getName();
        List<Transacao> transacoes = transacaoRepository
                .findByCompradorEmail(email);
        model.addAttribute("transacoes", transacoes);
        return "ingresso/comprados";
    }

/**
 * Pesquisa ingressos com filtro, paginação e ordenação.
 */
// @GetMapping("/pesquisar")
// public String pesquisar(
//     IngressoFilter filtro,
//     @PageableDefault(size = 10)
//     @SortDefault(sort = "id", direction = Sort.Direction.ASC)
//     Pageable pageable,
//     HttpServletRequest request,
//     Model model
// ) {
//     // chama seu método customizado de pesquisa
//     Page<Ingresso> pagina = ingressoRepository.pesquisar(filtro, pageable);

//     // embrulha para a UI
//     model.addAttribute("pagina", new PageWrapper<>(pagina, request));
//     model.addAttribute("filtro", filtro);

//     // detalhes da ordenação usados no template
//     String sortField = pageable.getSort().iterator().hasNext()
//         ? pageable.getSort().iterator().next().getProperty()
//         : "id";
//     String sortDir   = pageable.getSort().iterator().hasNext()
//         ? (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc")
//         : "asc";
//     model.addAttribute("sortField", sortField);
//     model.addAttribute("sortDir",   sortDir);
//     model.addAttribute("reverseDir", sortDir.equals("asc") ? "desc" : "asc");

//     // reutilizamos a mesma view de listar
//     return "ingresso/listar";

// }
}