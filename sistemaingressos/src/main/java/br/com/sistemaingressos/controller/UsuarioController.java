package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.UsuarioRepository;
import br.com.sistemaingressos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Cadastrar usuários
     @GetMapping("/registrar")
    public String registrarForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }


    @PostMapping("/registrar")
    public String registrarSubmit(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/login";
    }


    // LISTAR usuários
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.getAllUsuarios());
        return "usuario/listar"; 
    }

    // FORMULÁRIO novo usuário
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/formulario";
    }

    // SALVAR usuário
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Usuario usuario) {
        usuarioService.createUsuario(usuario);
        return "redirect:/usuarios";
    }

    // EDITAR usuário
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.getUsuarioById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuario/formulario";
    }

    // EXCLUIR usuário
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return "redirect:/usuarios";
    }
}
