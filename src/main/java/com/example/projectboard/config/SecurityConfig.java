
package com.example.projectboard.config;

import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.dto.security.BoardPrincipal;
import com.example.projectboard.dto.security.KakaoOAuth2Response;
import com.example.projectboard.repository.UserAccountRepository;
import com.example.projectboard.service.UserAccountService;
import java.util.Random;
import java.util.UUID;
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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() //정적 리소스
                .requestMatchers(HttpMethod.GET, "/", "/articles", "/articles/search-hashtag", "/articles/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                .anyRequest().authenticated())
            .formLogin(Customizer.withDefaults())
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll()) //logout -> redirect
            .oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))); //OAuth2.0
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
    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
        return username ->
            userAccountService.searchUser(username)
            .map(BoardPrincipal::from)
            .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다" + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(UserAccountService userAccountService, PasswordEncoder passwordEncoder){
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest ->  {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            KakaoOAuth2Response kakaoOAuth2Response = KakaoOAuth2Response.from(oAuth2User.getAttributes());

            String registrationId = userRequest.getClientRegistration().getRegistrationId(); //고유값("kakao")
            String providerId = kakaoOAuth2Response.id().toString();

            String username = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode("{bcrypt}dummy"+ UUID.randomUUID());

            return userAccountService.searchUser(username)
                .map(BoardPrincipal::from)
                .orElseGet(
                    () -> BoardPrincipal.from(
                        userAccountService.saveUser(
                            username,
                            dummyPassword,
                            kakaoOAuth2Response.email(),
                            kakaoOAuth2Response.nickname(),
                            kakaoOAuth2Response.nickname() + "memo"
                            )
                    )
                );
        };
    }

    /**
     * 보안 상 향후 수정
     */
    @Bean
    public PasswordEncoder noOpPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}