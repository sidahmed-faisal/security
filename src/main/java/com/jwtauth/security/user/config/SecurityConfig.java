package com.jwtauth.security.user.config;

import com.jwtauth.security.user.filter.TokenAuthenticationFilter;
import com.jwtauth.security.user.service.NoOpsRedirectStrategy;
import com.jwtauth.security.user.service.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/user/**"),
            new AntPathRequestMatcher("/actuator/**")
    );
    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
    private  final TokenProvider tokenProvider;

    @Autowired
    public SecurityConfig(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(tokenProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(),PROTECTED_URLS)
                    .and()
                    .authenticationProvider(tokenProvider)
                    .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                        .authorizeRequests()
                        .requestMatchers(PROTECTED_URLS)
                        .authenticated()
                        .and()
                        .csrf().disable()
                        .formLogin().disable()
                        .httpBasic().disable()
                        .logout().disable();
    }
    @Bean
    TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler(){
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler().setRedirectStrategy(new NoOpsRedirectStrategy());
        return successHandler;
    }

    @Bean
    public AuthenticationEntryPoint forbiddenEntryPoint(){
        return new HttpStatusEntryPoint(FORBIDDEN);
    }
}
