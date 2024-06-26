package org.poolc.api.auth.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.domain.JwtAuthenticationFilter;
import org.poolc.api.auth.infra.JwtTokenProvider;
import org.poolc.api.member.domain.MemberRole;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList(
                "https://alpha.poolc.org", "http://localhost:3000",
                "https://server.poolc.kr", "http://server.poolc.kr",
                "http://poolc.kr","https://poolc.kr",
                "https://poolc.org", "http://poolc.org","https://poolc.org/api", "http://poolc.org/api",
                "https://dev.poolc.org", "http://dev.poolc.org","https://dev.poolc.org/api", "http://dev.poolc.org/api",
                "chrome-extension://doeamknhlolnflkmhbhkagganhjjbefe"
        ));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Cache-Control",
                "Content-Type", "Accept", "Content-Length", "Accept-Encoding", "X-Requested-With"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.POST, "/project").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/project/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/project/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/project/**").permitAll()
                .antMatchers("/swagger-ui/*").permitAll()

                .antMatchers(HttpMethod.POST, "/board").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/board/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/board/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/board/**").permitAll()

                .antMatchers(HttpMethod.GET, "/comment").permitAll()
                .antMatchers(HttpMethod.GET, "/comment/post/*").permitAll()
                .antMatchers(HttpMethod.GET, "/comment/*").permitAll()
                .antMatchers("/comment/**").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.GET, "/post").permitAll()
                .antMatchers(HttpMethod.POST, "/post/new").hasAnyAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.GET, "/post/board/*").permitAll()
                .antMatchers(HttpMethod.GET, "/post/*").permitAll()
                .antMatchers(HttpMethod.GET, "/post/search").permitAll()
                .antMatchers(HttpMethod.POST, "/post/search").permitAll()
                .antMatchers(HttpMethod.POST, "/post/new").permitAll()
                .antMatchers("/post/**").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.GET, "/book").permitAll()
                .antMatchers(HttpMethod.GET, "/book/*").permitAll()
                .antMatchers(HttpMethod.POST, "/book").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/book/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/book/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/book/borrow/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.PUT, "/book/return/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers("/book/**").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.POST, "/member").permitAll()
                .antMatchers(HttpMethod.GET, "/member/me").not().hasAuthority(MemberRole.EXPELLED.name())
                .antMatchers(HttpMethod.GET, "/member/role").not().hasAuthority(MemberRole.EXPELLED.name())
                .antMatchers(HttpMethod.PUT, "/member/reset-password-token").permitAll()
                .antMatchers(HttpMethod.PUT, "/member/reset-password").permitAll()
                .antMatchers(HttpMethod.GET, "/member").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers("/member/activate/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/member/unaccepted").hasAnyAuthority(MemberRole.ADMIN.name())
                .antMatchers("/member/**").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.POST, "/poolc").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/poolc").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/poolc/**").permitAll()

                .antMatchers(HttpMethod.GET, "/activity").permitAll()
                .antMatchers(HttpMethod.GET, "/activity/*").permitAll()
                .antMatchers(HttpMethod.GET, "/activity/session/activity/*").permitAll()
                .antMatchers(HttpMethod.GET, "/activity/session/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/activity/open/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/activity/close/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/activity/check/*").permitAll()
                .antMatchers(HttpMethod.GET, "/activity/member/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/activity").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/activity/session").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/activity/apply/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/activity/check/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.PUT, "/activity/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.PUT, "/activity/session/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.DELETE, "/activity/*").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers("/file").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.GET,"/room/*").hasAnyAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST,"/room/*").hasAnyAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.PUT,"/room/*").hasAnyAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.DELETE,"/room/*").hasAnyAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.GET,"/badge").hasAnyAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST,"/badge").hasAnyAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/badge").hasAnyAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE,"/badge").hasAnyAuthority(MemberRole.ADMIN.name())

                .antMatchers(HttpMethod.POST, "/interview/application/*").hasAuthority(MemberRole.UNACCEPTED.name())
                .antMatchers(HttpMethod.DELETE, "/interview/application/*").hasAnyAuthority(MemberRole.UNACCEPTED.name(), MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/interview/slots").permitAll()
                .antMatchers(HttpMethod.POST, "/interview/slots").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/interview/slots/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/interview/slots/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/interview/slots").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/**").permitAll()

                .anyRequest().authenticated().and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);
    }
}
