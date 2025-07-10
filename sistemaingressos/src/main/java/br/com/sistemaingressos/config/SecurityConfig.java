package br.com.sistemaingressos.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //             .authorizeHttpRequests(auth -> auth
    //                     .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/index.html").permitAll()
    //                     .requestMatchers("/eventos/novo", "/eventos/salvar").hasRole("ADMIN")
    //                     .requestMatchers("/usuarios/**").hasRole("ADMIN")
    //                     .anyRequest().authenticated())
    //             .formLogin(form -> form
    //                     .loginPage("/login")
    //                     .defaultSuccessUrl("/ingressos/anunciar", true)
    //                     .permitAll())
    //             .logout(logout -> logout
    //                     .logoutUrl("/logout")
    //                     .logoutSuccessUrl("/") // redireciona para página inicial após logout
    //                     .invalidateHttpSession(true)
    //                     .deleteCookies("JSESSIONID"))

    //             .sessionManagement(sess -> sess
    //                     .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

    //     return http.build();
    // }

    /**
     * PasswordEncoder simples: compara texto puro.
     * Apenas para desenvolvimento e testes rápidos!
     */
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return NoOpPasswordEncoder.getInstance();
    // }


    // NOVA TENTATIVA - para criptografia COM BCRPT
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt com strength padrão (10)
        return new BCryptPasswordEncoder();
    }

    /**
     * JDBC UserDetailsService para autenticar pelo campo "email".
     */
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
    JdbcUserDetailsManager mgr = new JdbcUserDetailsManager(dataSource);

    // busca usuário pelo email
    mgr.setUsersByUsernameQuery(
    "SELECT email, senha, ativo FROM usuario WHERE email = ?");
    // busca papéis / authorities do usuário
    mgr.setAuthoritiesByUsernameQuery(
    "SELECT u.email AS username, p.nome AS authority " +
    "FROM usuario u " +
    "JOIN usuario_papel up ON u.id = up.usuario_id " +
    "JOIN papel p ON up.papel_id = p.id " +
    "WHERE u.email = ?");

    return mgr;
    }

//     @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     http
//         // --------- exigir HTTPS em rotas críticas ----------
//         .requiresChannel(channel -> channel
//             // somente por HTTPS em /usuarios/** e /ingressos/**
//             .requestMatchers("/usuarios/**", "/ingressos/**")
//             .requiresSecure()
//         )
//         // --------- regras de acesso por papel ---------------
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/error","/css/**", "/js/**", "/images/**", "/", "/index.html").permitAll()
//             .requestMatchers("/eventos/novo", "/eventos/salvar").hasRole("ADMIN")
//             .requestMatchers("/usuarios/**").hasRole("ADMIN")
//             .anyRequest().authenticated()
//         )
//         // --------- configuração de formulário de login ------
//         .formLogin(form -> form
//             .loginPage("/login")
//             .defaultSuccessUrl("/ingressos/anunciar", true)
//             .permitAll()
//         )
//         .logout(logout -> logout.permitAll());
    
//     return http.build();
// }

 @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth

        // 1) endpoints públicos
        .requestMatchers("/login", "/error", "/", "/index.html",
                         "/css/**", "/js/**", "/images/**")
          .permitAll()

        // 2) transações do próprio usuário (comum ou admin)
        .requestMatchers("/transacoes/minhas")
          .authenticated()

        // 3) rotas administrativas (só ADMIN)
        .requestMatchers(
            "/eventos", "/eventos/novo", "/eventos/salvar",
            "/eventos/editar/**", "/eventos/excluir/**",
            "/ingressos", "/transacoes/**", "/usuarios/**"
        ).hasRole("ADMIN")

        // 4) demais rotas de “usuário comum” (já logado)
        .requestMatchers(
            "/eventos/publicos",
            "/ingressos/comprar",
            "/ingressos/anunciados",
            "/ingressos/comprados",
            "/ingressos/vender"
        ).authenticated()

        // 5) qualquer outra requisição exige autenticação
        .anyRequest().authenticated()
      )

      // exigir HTTPS nas partes críticas
      .requiresChannel(channel -> channel
        .requestMatchers("/login",
                         "/eventos/**",
                         "/ingressos/**",
                         "/transacoes/**",
                         "/usuarios/**")
          .requiresSecure()
      )

      // formulário de login
      .formLogin(form -> form
        .loginPage("/login")
        .defaultSuccessUrl("/eventos/publicos", true)
        .permitAll()
      )

      // logout
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .deleteCookies("JSESSIONID")
        .permitAll()
      );

    return http.build();
}


}
