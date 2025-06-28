package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Transacao;
import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.TransacaoRepository;
import br.com.sistemaingressos.repository.IngressoRepository;
import br.com.sistemaingressos.repository.UsuarioRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private IngressoRepository ingressoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("transacoes", transacaoRepository.findAll());
        return "transacao/listar";
    }

    // FORMULÁRIO
    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("transacao", new Transacao());
        model.addAttribute("ingressos", ingressoRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "transacao/formulario";
    }

    // SALVAR
    @PostMapping("/salvar")
    public String salvar(@Valid Transacao transacao) {
        transacaoRepository.save(transacao);
        return "redirect:/transacoes";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Transacao transacao = transacaoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("transacao", transacao);
        model.addAttribute("ingressos", ingressoRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "transacao/formulario";
    }

    // EXCLUIR
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        transacaoRepository.deleteById(id);
        return "redirect:/transacoes";
    }
}
