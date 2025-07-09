package br.com.sistemaingressos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class PasswordMigrationRunner implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public PasswordMigrationRunner(UsuarioRepository usuarioRepo,
                                   PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        for (Usuario u : usuarioRepo.findAll()) {
            String raw = u.getSenha();
            // só re-encoda quem não parece já criptografado BCrypt
            if (raw != null && !raw.startsWith("$2a$") && !raw.startsWith("$2b$")) {
                String hashed = passwordEncoder.encode(raw);
                u.setSenha(hashed);
                usuarioRepo.save(u);
                System.out.printf("Migrated password for user %s%n", u.getEmail());
            }
        }
    }
}
