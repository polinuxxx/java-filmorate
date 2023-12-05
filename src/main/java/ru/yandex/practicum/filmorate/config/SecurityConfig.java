package ru.yandex.practicum.filmorate.config;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                        + " from users "
                        + " where login = ? ")
                .authoritiesByUsernameQuery("select users.login, authorities.authority "
                        + " from users "
                        + " left join authorities on users.id = authorities.user_id"
                        + " where users.login = ? ");
    }


    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http
                .csrf()
                .disable()
                .httpBasic(withDefaults())
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/registration").not().fullyAuthenticated()
                //Доступ только для пользователей с Authority CLIENT
                //.antMatchers("/ui/**").hasAuthority("CLIENT")
                //Доступ разрешен всем пользователям
                .antMatchers("/**", "/resources/**").permitAll()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                //Настройка для входа в систему
                .formLogin(loginConfigurer -> loginConfigurer
                        .defaultSuccessUrl("/ui/").permitAll())
                //Перенарпавление на главную страницу после успешного входа
                .logout()
                .permitAll()
                .logoutSuccessUrl("/");

        return http.build();
    }

}
