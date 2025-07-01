package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1) Mostrar perfil
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model) {
        Usuario usuario = usuarioService.getUsuarioById(1L).orElse(new Usuario());
        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

    // 2) Atualizar perfil
    @PostMapping("/perfil")
    public String salvarPerfil(@ModelAttribute Usuario usuario, Model model) {
        usuarioService.atualizarUsuario(usuario);
        model.addAttribute("sucesso", "Perfil atualizado com sucesso!");
        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

    // 3) Novo cadastro
    @GetMapping("/novo")
    public String novoUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "novo-usuario";
    }

    @PostMapping("/novo")
    public String salvarNovoUsuario(@ModelAttribute Usuario usuario, Model model) {
        usuarioService.salvarUsuario(usuario);
        model.addAttribute("sucesso", "Usuário cadastrado com sucesso!");
        return "login"; // Redireciona pra login ou outra página
    }

    // 4) Deletar conta
    @PostMapping("/deletar")
    public String deletarUsuario(@RequestParam Long id) {
        usuarioService.deletarUsuario(id);
        return "redirect:/logout"; // Por exemplo, desloga depois de deletar
    }
}
