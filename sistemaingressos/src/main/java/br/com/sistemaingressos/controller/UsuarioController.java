package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.model.Papel;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.PapelRepository;
import br.com.sistemaingressos.repository.UsuarioRepository;
import br.com.sistemaingressos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    // Form de registro público
    @GetMapping("/registrar")
    public String registrarForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }

    @PostMapping("/registrar")
    public String registrarSubmit(@ModelAttribute Usuario usuario) {
        usuarioService.createUsuario(usuario);
        return "redirect:/login";
    }

    // LISTAR usuários
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.getAllUsuarios());
        return "usuario/listar";
    }

    // FORMULÁRIO novo usuário (ADMIN)
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("todosPapeis", papelRepository.findAll());
        return "usuario/formulario";
    }

    // SALVAR usuário (ADMIN)
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Usuario usuario,
                         @RequestParam(name = "papeisIds", required = false) List<Long> papeisIds) {
        // Carrega papéis do banco
        Set<Papel> papeis = new HashSet<>();
        if (papeisIds != null) {
            papelRepository.findAllById(papeisIds).forEach(papeis::add);
        }
        usuario.setPapeis(papeis);

        // Cria (ou atualiza) com lógica de senha/ativo no service
        usuarioService.createUsuario(usuario);
        return "redirect:/usuarios";
    }

    // EDITAR usuário
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.getUsuarioById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("todosPapeis", papelRepository.findAll());
        return "usuario/formulario";
    }

    // EXCLUIR usuário
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return "redirect:/usuarios";
    }
}
