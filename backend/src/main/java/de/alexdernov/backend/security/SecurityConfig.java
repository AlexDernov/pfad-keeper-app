package de.alexdernov.backend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${app.environment}")
    private String environment;

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/user/").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/games").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/games/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/games/*/prompt").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/games/*/generateImage").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/games/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/lobbies").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/lobbies/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/lobbies/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/lobbies/*/join").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/lobbies/*/leave").authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .oauth2Login(oauth2 -> {
                    try {
                        oauth2.init(http);
                        if (environment.equals("prod")) {
                            oauth2.defaultSuccessUrl("/", true);
                        } else {
                            oauth2.defaultSuccessUrl("http://localhost:5173", true);
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .logout(logout -> logout.logoutUrl("/api/users/logout")
                        .logoutSuccessHandler((request, response, authentication) ->
                                response.setStatus(HttpServletResponse.SC_OK)
                        ));
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return request -> {
            OAuth2User user = delegate.loadUser(request);

            if (userService.saveNewUser(user)) {
                return user;
            }

            return null;
        };
    }
}
