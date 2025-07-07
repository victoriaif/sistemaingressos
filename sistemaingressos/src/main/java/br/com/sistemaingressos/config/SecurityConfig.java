package br.com.sistemaingressos.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/index.html").permitAll()
                .requestMatchers("/eventos/novo", "/eventos/salvar").hasRole("ADMIN")
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")  //redireciona para página inicial após logout
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            )

            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            );

        return http.build();
    }

    /**
     * PasswordEncoder simples: compara texto puro.
     * Apenas para desenvolvimento e testes rápidos!
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * JDBC UserDetailsService para autenticar pelo campo "email".
     */
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager mgr = new JdbcUserDetailsManager(dataSource);

        // busca usuário pelo email
        mgr.setUsersByUsernameQuery(
            "SELECT email, senha, ativo FROM usuario WHERE email = ?"
        );
        // busca papéis / authorities do usuário
      mgr.setAuthoritiesByUsernameQuery(
    "SELECT u.email AS username, p.nome AS authority " +
    "FROM usuario u " +
    "JOIN usuario_papel up ON u.id = up.usuario_id " +
    "JOIN papel p ON up.papel_id = p.id " +
    "WHERE u.email = ?"
);


        return mgr;
    }
}
