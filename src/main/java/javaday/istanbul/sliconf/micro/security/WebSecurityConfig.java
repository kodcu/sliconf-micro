package javaday.istanbul.sliconf.micro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JWTAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests()
                .antMatchers("/favicon.ico").anonymous()
                .antMatchers("/").permitAll()
                .antMatchers("/service/users/login").permitAll()
                .antMatchers("/service/users/login/anonymous/**").permitAll()
                .antMatchers("/service/users/register").permitAll()
                .antMatchers("/service/users/register/anonymous/**").permitAll()
                .antMatchers("/service/swagger").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/service/**")
                .access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
                .and()
                // And filter other requests to check the presence of JWT in header
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/")
                .logoutUrl("/logout")
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/service/**");
    }
}