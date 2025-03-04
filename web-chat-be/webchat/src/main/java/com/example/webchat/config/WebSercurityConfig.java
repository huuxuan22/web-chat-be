package com.example.webchat.config;

import com.example.webchat.filter.JwtTokenFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // quan ly quyen truy cap trai phep, han che cac truy cap tan cong
public class WebSercurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public MappingJackson2HttpMessageConverter jsonConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(AbstractHttpConfigurer:: disable)
                .addFilterBefore((Filter) jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((request)   -> {

                    request.requestMatchers(
                                    "**")

                            .permitAll()
                            // phaan quyen cho user

                            // phan quyen cho feedback

                            .anyRequest().authenticated()
                    ;
                })
                .csrf(AbstractHttpConfigurer::disable);
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {// dùng để tùy chỉnh cấu hình Cors
            /**
             * cau hinh thiet lap CORS cho ung dung
             * @param httpSecurityCorsConfigurer doi tuong CorsConfigurer de cau hinh bao mat CORS cho ung dung
             */
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*")); // cho phép tất cả domain gửi yêu cầu
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
                // cho phép cái http nhất định đi qua
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token")); // liệt kê các header mà front end có thể truycaapjpj từ phản hồi
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                // tạo 1 nguồn cấu hình CORS dựa trên URl
                source.registerCorsConfiguration("/**", configuration);// dùng đ cấu hình CORS cho tất cả đường dẫn
                httpSecurityCorsConfigurer.configurationSource(source); // áp dụng cấu hình để bỏ vào httpSecurityCorsConfigurer
            }
        }) ;
        return http.build();
    }
}
