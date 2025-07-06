package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.UsuarioRepository;
import br.com.sistemaingressos.repository.PapelRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * Cria um novo usuário (ou atualiza existente), codifica a senha e ativa o registro.
     */
    public Usuario createUsuario(Usuario usuario) {
        // Codifica senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        // Marca ativo
        usuario.setAtivo(true);
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
}
