package net.orekyuu.workbench.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WorkbenchSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //ログイン画面は常にアクセス許可 それ以外は要検証
        http.authorizeRequests().antMatchers("/login", "/signup", "/favicon.ico").permitAll()
            .anyRequest().authenticated();
        //ログインの設定
        http.formLogin().loginProcessingUrl("/login").loginPage("/login")
            .failureUrl("/login?error").defaultSuccessUrl("/")
            .usernameParameter("username").passwordParameter("password")
            .permitAll();
        //ログアウトの設定
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login")
            .permitAll();
        //CSRFトークンをCookieに保存する(X-CSRF-TOKENに保存される)
        http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //静的リソースは常にアクセス許可
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/plugins/**", "/fonts/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
