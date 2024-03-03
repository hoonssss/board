
package com.example.projectboard.config;

import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.dto.security.BoardPrincipal;
import com.example.projectboard.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() //정적 리소스
//                .requestMatchers(HttpMethod.GET, "/", "/articles", "articles/search-hashtag").permitAll() // 접근 허용
//                .anyRequest().authenticated())
//            .formLogin(Customizer.withDefaults())
//            .logout(logout -> logout.logoutSuccessUrl("/")) //logout -> redirect
//            .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/articles", "/articles/search-hashtag", "/articles/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                .anyRequest().authenticated())
            .formLogin(Customizer.withDefaults())
            .logout(logout -> logout
                .logoutSuccessUrl("/").permitAll());
        return http.build();
    }

    /**
     * security 검사 제외
     * securityFilterChain requestMatchers 이동
     **/
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web -> web.ignoring()
//            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
//    }

    /**
     * 실제 인증 데이터를 가져오는 비즈니스 로직 구현
     */
    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository.findById(username)
            .map(UserAccountDto::from)
            .map(BoardPrincipal::from)
            .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다" + username));
    }

    @Bean
    public PasswordEncoder noOpPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}