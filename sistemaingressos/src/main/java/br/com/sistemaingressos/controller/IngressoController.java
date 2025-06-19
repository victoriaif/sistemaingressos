package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.repository.IngressoRepository;
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
    private IngressoRepository ingressoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    // Formulário para novo ingresso
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("ingresso", new Ingresso());
        model.addAttribute("eventos", eventoRepository.findAll());
        return "ingresso/formulario";
    }

    // Salvar ingresso
    @PostMapping("/salvar")
    public String salvar(@Valid Ingresso ingresso) {
        ingressoRepository.save(ingresso);
        return "redirect:/ingressos";
    }
}
