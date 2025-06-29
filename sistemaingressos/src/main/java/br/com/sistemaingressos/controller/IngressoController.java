package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.repository.IngressoRepository;
import br.com.sistemaingressos.service.IngressoService;
import br.com.sistemaingressos.repository.EventoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/ingressos")
public class IngressoController {

    @Autowired
    // private IngressoRepository ingressoRepository;
    private IngressoService ingressoService;

    //Dúvida: nos Controller: quando eu devo chamar o “classeRepository” e/ou o “classeService”?
    //Dúvida: Como eu identifico pra quais classes eu devo criar um Service?

    @Autowired
    private EventoRepository eventoRepository;


   // LISTAR todos os ingressos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ingressos", ingressoService.listarTodos());
        return "ingresso/listar";
    }

    // Formulário para novo ingresso
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("ingresso", new Ingresso());
        model.addAttribute("eventos", eventoRepository.findAll());
        return "ingresso/formulario";
    }

    // SALVAR novo ingresso ou atualizar existente
    @PostMapping("/salvar")
    public String salvar(@Valid Ingresso ingresso) {
        ingressoService.salvar(ingresso);
        return "redirect:/ingressos";
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
}