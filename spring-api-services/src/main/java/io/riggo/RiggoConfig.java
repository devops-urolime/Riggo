package io.riggo;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import io.riggo.web.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity(debug = true)
public class RiggoConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.apiAudience}")
    private String apiAudience;
    @Value(value = "${auth0.issuer}")
    private String issuer;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        JwtWebSecurityConfigurer
                .forRS256(apiAudience, issuer)
                .configure(http)
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, Paths.API_VERSION_LOAD_PIPELINE_SUMMARY).authenticated()
//                .antMatchers(HttpMethod.GET, Paths.API_VERSION_LOAD_PIPELINE_SUMMARY).hasAuthority("read:loadPipeline")
//                .antMatchers(HttpMethod.GET, Paths.API_VERSION_LOAD + "/**").authenticated()
//                .antMatchers(HttpMethod.GET, Paths.API_VERSION_LOAD + "/**").hasAuthority("read:load")
//                .antMatchers(HttpMethod.POST, Paths.API_VERSION_LOAD + "/**").hasAuthority("write:load")
//                .antMatchers(HttpMethod.GET, Paths.API_VERSION_MENUS + "/menus").authenticated()
//                .antMatchers(HttpMethod.PUT, Paths.API_VERSION_LOAD + "/**").authenticated()
//                .antMatchers(HttpMethod.PUT, Paths.API_VERSION_LOAD + "/**").hasAuthority("write:load")
//                .antMatchers(HttpMethod.PATCH, Paths.API_VERSION_LOAD + "/**").authenticated()
//                .antMatchers(HttpMethod.PATCH, Paths.API_VERSION_LOAD + "/**").hasAuthority("write:load");
        ;

    }
}
