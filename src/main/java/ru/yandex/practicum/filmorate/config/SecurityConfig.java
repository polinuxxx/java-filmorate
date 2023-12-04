package ru.yandex.practicum.filmorate.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Конфиг для аутентификации.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    final DataSource dataSource;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @SneakyThrows
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(" select login, password, enabled "
                        + " from public.users "
                        + " where login = ? ")
                .authoritiesByUsernameQuery("select users.login, authorities.authority "
                        + " from users "
                        + " join authorities on users.id = authorities.user_id"
                        + " where users.login = ? ");
    }

    @SneakyThrows
    @Bean
    SecurityFilterChain adminFilterChain(HttpSecurity http) {

//        http.authorizeRequests()
//                .antMatchers("/films/**", "/login").permitAll()
//                .antMatchers("/ui/**").hasRole("CLIENT")
//                .and()
//                .formLogin(loginConfigurer -> loginConfigurer
//                        .defaultSuccessUrl("/ui/"))
//                .csrf().disable();

        return http.build();
    }
}
