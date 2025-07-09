package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.UsuarioRepository;
import br.com.sistemaingressos.repository.PapelRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//pra setar o usuario logado no ingresso que será anunciando
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            PapelRepository papelRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Cria um novo usuário (ou atualiza existente), codifica a senha e ativa o
     * registro.
     */
    public Usuario createUsuario(Usuario usuario) {
        // ✅ Verifica se data de nascimento está no futuro
        if (usuario.getDataNascimento() != null && usuario.getDataNascimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
        }

        // ✅ Verifica se email já existe (em novo cadastro ou edição com outro email)
        boolean emailJaExiste = usuarioRepository.existsByEmail(usuario.getEmail());
        if (emailJaExiste && (usuario.getId() == null || !usuarioRepository.findById(usuario.getId())
                .map(u -> u.getEmail().equals(usuario.getEmail())).orElse(false))) {
            throw new IllegalArgumentException("O e-mail já está em uso por outro usuário.");
        }

        // ✅ Verifica se CPF já existe
        boolean cpfJaExiste = usuarioRepository.existsByCpf(usuario.getCpf());
        if (cpfJaExiste && (usuario.getId() == null || !usuarioRepository.findById(usuario.getId())
                .map(u -> u.getCpf().equals(usuario.getCpf())).orElse(false))) {
            throw new IllegalArgumentException("O CPF já está em uso por outro usuário.");
        }

        // ✅ Codifica a senha se for novo usuário (sem id)
        if (usuario.getId() == null) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            usuario.setAtivo(true); // define como ativo por padrão
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Regra de negócio - Ingresso: setar o usuário logado no ingresso anunciado
    public Usuario getUsuarioLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }
}
