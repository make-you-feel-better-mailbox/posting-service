package com.onetwo.postservice.common.config;

import com.onetwo.postservice.common.GlobalUrl;
import com.onetwo.postservice.common.config.filter.FilterConfigure;
import com.onetwo.postservice.common.jwt.JwtAccessDeniedHandler;
import com.onetwo.postservice.common.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final FilterConfigure filterConfigure;

    private static final String[] WHITE_LIST = {
            "/favicon.ico", "/docs/**"
    };

    @Bean
    public MvcRequestMatcher.Builder mvcRequestMatcherBuilder(HandlerMappingIntrospector introspect) {
        return new MvcRequestMatcher.Builder(introspect);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // enable h2-console
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)     // 세션을 사용하지 않기 때문에 STATELESS로 설정
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(PathRequest.toH2Console()).permitAll()// h2-console, favicon.ico 요청 인증 무시
                                .requestMatchers(this.createMvcRequestMatcherForWhitelist(mvc)).permitAll()
                                .anyRequest().authenticated() // 그 외 인증 없이 접근X
                )
                .apply(filterConfigure); // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용

        return httpSecurity.build();
    }

    private MvcRequestMatcher[] createMvcRequestMatcherForWhitelist(MvcRequestMatcher.Builder mvc) {
        List<MvcRequestMatcher> mvcRequestMatcherList = Stream.of(WHITE_LIST).map(mvc::pattern).collect(Collectors.toList());

        mvcRequestMatcherList.add(mvc.pattern(HttpMethod.GET, GlobalUrl.POSTING_FILTER));

        return mvcRequestMatcherList.toArray(MvcRequestMatcher[]::new);
    }
}
