package br.com.sistemaingressos.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

// import java.util.HashMap;
// import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(configurer -> configurer
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/index.html")
                                                .permitAll()
                                                .requestMatchers("/eventos/novo", "/eventos/salvar").hasRole("ADMIN") // Exemplo
                                                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessHandler(
                                                                (request, response, authentication) -> response
                                                                                .addHeader("HX-Redirect", "/"))
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

                return http.build();
        }

        /**
         * Configura o JDBC UserDetailsManager para buscar pelo campo `email`.
         */
        @Bean
        public UserDetailsService userDetailsService(DataSource dataSource) {
                JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

                // 1) Buscar usuário pelo email
                manager.setUsersByUsernameQuery(
                                "SELECT email, senha, ativo " +
                                                "FROM usuario " +
                                                "WHERE email = ?");

                // 2) Buscar papéis do usuário (note os nomes de colunas corretos)
                manager.setAuthoritiesByUsernameQuery(
                                "SELECT u.email, p.nome " +
                                                "FROM usuario u " +
                                                "JOIN usuario_papel up ON u.id = up.usuario_id " +
                                                "JOIN papel p           ON up.papel_id = p.id " +
                                                "WHERE u.email = ?");

                return manager;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
