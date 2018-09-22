package com.example.web.config;

import com.example.web.oauth.ClientResources;
import com.example.web.oauth.SocialType;
import com.example.web.oauth.UserTokenService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * https://github.com/young891221/spring-boot-social-comment
 */

@Slf4j
@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;


    @Bean
    public RequestContextListener requestContextListener()
    {
        return new RequestContextListener();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeRequests()
                .antMatchers("/", "/login/**", "/fail").permitAll()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("SESSION")
                .invalidateHttpSession(true)
                .and()
                .addFilterBefore(filter, CsrfFilter.class)
                .addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class)
                .csrf().disable();
    }



    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }


    private Filter oauth2Filter()
    {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(oauth2Filter(facebook(), "/login/facebook", SocialType.FACEBOOK));
        filters.add(oauth2Filter(google(), "/login/google", SocialType.GOOGLE));
        filters.add(oauth2Filter(kakao(), "/login/kakao", SocialType.KAKAO));
        filters.add(oauth2Filter(naver(), "/login/naver", SocialType.NAVER));
        filter.setFilters(filters);
        return filter;
    }


    public static class OAuth2ClientAuthenticationProcessingFilterWarp extends OAuth2ClientAuthenticationProcessingFilter
    {

        public OAuth2ClientAuthenticationProcessingFilterWarp(String defaultFilterProcessesUrl)
        {
            super(defaultFilterProcessesUrl);
        }

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
            super.doFilter(req, res, chain);
        }

        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException
        {
            log.info("successfulAuthentication => {}", request.getRequestURI());
            super.successfulAuthentication(request, response, chain, authResult);
        }

        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException
        {
            log.info("unsuccessfulAuthentication => {}", request.getRequestURI());
            log.info("unsuccessfulAuthentication => {}", response.getStatus());
            log.error("unsuccessfulAuthentication => {}", Throwables.getStackTraceAsString(failed));
            log.error("unsuccessfulAuthentication => {}", Throwables.getStackTraceAsString(failed.getCause()));
            log.error("unsuccessfulAuthentication => {}", Throwables.getStackTraceAsString(failed.getCause().getCause()));
            super.unsuccessfulAuthentication(request, response, failed);
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException
        {
            log.info("attemptAuthentication {} => {} {}", request.getRequestURI(), request.getQueryString(), request.getRequestURI());
            log.info("attemptAuthentication => {}", response.getStatus());
            Authentication authen = super.attemptAuthentication(request, response);
            log.info("Authentication.isAuthenticated() = {}", authen.isAuthenticated());
            log.info("Authentication.getPrincipal() = {}", authen.getPrincipal());
            return authen;
        }
    }


    public static class OAuth2RestTemplateWarp extends OAuth2RestTemplate
    {
        public OAuth2RestTemplateWarp(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
            super(resource, context);
        }

        @Override
        public OAuth2AccessToken getAccessToken() throws UserRedirectRequiredException {
            log.info("getAccessToken()");
            return super.getAccessToken();
        }
    }


    private Filter oauth2Filter(ClientResources client, String path, SocialType socialType)
    {
        OAuth2RestTemplate template = new OAuth2RestTemplateWarp(client.getClient(), oAuth2ClientContext);
        // 로그인 성공 시
        StringBuilder redirectUrl = new StringBuilder("/");
        redirectUrl.append(socialType.getValue()).append("/complete");

        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilterWarp(path);
        filter.setRestTemplate(template);
        filter.setTokenServices(new UserTokenService(client, socialType));
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> response.sendRedirect(redirectUrl.toString()));
        filter.setAuthenticationFailureHandler((request, response, exception) -> response.sendRedirect("/fail"));
        return filter;
    }



    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook()
    {
        return new ClientResources();
    }



    @Bean
    @ConfigurationProperties("google")
    public ClientResources google()
    {
        return new ClientResources();
    }



    @Bean
    @ConfigurationProperties("kakao")
    public ClientResources kakao()
    {
        return new ClientResources();
    }


    @Bean
    @ConfigurationProperties("naver")
    public ClientResources naver()
    {
        return new ClientResources();
    }



}
