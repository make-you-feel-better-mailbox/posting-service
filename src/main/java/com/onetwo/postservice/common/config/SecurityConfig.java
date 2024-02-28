package com.onetwo.postservice.common.config;

import com.onetwo.postservice.common.GlobalUrl;
import onetwo.mailboxcommonconfig.common.RequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public RequestMatcher requestMatcher(MvcRequestMatcher.Builder mvc) {
        return new RequestMatcher() {
            @Override
            public List<MvcRequestMatcher> getMvcRequestMatcherArray() {
                List<MvcRequestMatcher> mvcRequestMatcherList = new ArrayList<>();

                mvcRequestMatcherList.add(mvc.pattern(HttpMethod.GET, GlobalUrl.POSTING_FILTER));

                mvcRequestMatcherList.add(mvc.pattern(HttpMethod.GET, GlobalUrl.POSTING_ROOT + GlobalUrl.EVERY_UNDER_ROUTE));

                return mvcRequestMatcherList;
            }
        };
    }
}
