package com.heeju.shop.config;

import com.heeju.shop.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // The imported AuthenticationManagerBuilder corresponds to the "AuthenticationManager" specified
    // in the Spring Security Authentication Architecture table(picture).

    @Autowired
    MemberService memberService;

    @Autowired
    UserDetailsService userDetailsService;

    // WebSecurityConfigurerAdapter alternative update code 1/2
    @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Tokenize the received http firstly. Refer to imports related to the keyword "Authenticate"

        log.info("------------- configure ------------- ");
        // Configure AuthenticationManagerBuilder
        // source: https://www.appsdeveloperblog.com/migrating-from-deprecated-websecurityconfigureradapter/
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(memberService).passwordEncoder(passwordEncoder());


        http.csrf().ignoringRequestMatchers("/h2-console/**");
        // Specify it when using H2-console without disabling CSRF.
        // source: https://github.com/HomoEfficio/dev-tips/blob/master/Spring%20Security%EC%99%80%20h2-console%20%ED%95%A8%EA%BB%98%20%EC%93%B0%EA%B8%B0.md

//        http.csrf().disable(); // Solve H2 connection and DB storage issues by specifying when using H2

        http.headers().frameOptions().disable(); // Solve the H2 LocalHost connection rejection problem by adding this

        // Write the http.formLogin() part. This part is considered the SecurityContextHolder area of
        // the SpringSecurity Authentication Architecture diagram. For this purpose, extends WebSecurityConfigurerAdaptor
        // had to be specified in SecurityConfig, but with the version update, just implementing filter
        // or WebSecurityCustomizer is OK.
        http.formLogin((formLogin) -> formLogin
                .loginPage("/members/login") // Set login page URL.
                .defaultSuccessUrl("/") // Set URL to redirect upon successful login.
                .usernameParameter("email") // Pass parameter ("email") to loadUserByUsername(String email) of service.MemberService.
                                            // Meanwhile, the token received from the UsernamePasswordAuthenticationToken pass
                                            // is pending in AuthenticationProvider(s).
                                            // If successful, execute Authentication Success Handler and redirect to defaultSuccessUrl() URL


                .failureUrl("/members/login/error")) // Set the URL to go to when login fails.
                                                                        // Run Authentication Failure Handler
                .logout((logout) -> logout // Logout URL settings
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/")  // URL to go to when logout is successful
                .invalidateHttpSession(true)); // Existing user sessions are also deleted through invalidateHttpSession.

        // http.authorizeRequest(): This means using HttpServletRequest for security processing.
        http.authorizeRequests()
                .requestMatchers("/favicon.ico", "/error").permitAll() // source: https://doongi9.tistory.com/entry/Spring-Boot-status999errorNonemessageNo-message-available
                .requestMatchers("/","/members/**","/item/**","/images/**").permitAll()
                // Set all users to access the path without authentication (login) through permitAll().
                // This includes the main page, member-related URL, product detail page to be created later,
                // and path to load product images.
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Paths starting with /admin are set to be accessible only when the account is in the ADMIN role.
                // Except for the path set above, all other paths are set to require authentication.
                .anyRequest().authenticated();
                // mvcMatchers() has been deprecated and replaced with requestMatcher since Spring Security 6.0.1.
                // source: https://github.com/spring-projects/spring-security/issues/12463

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        // Register a handler that is executed when an unauthenticated user accesses.
        // When using new BasicAuthenticationEntryPoint(), a login modal window appears.
        // Source: https://stackoverflow.com/questions/31424196/disable-browser-authentication-dialog-in-spring-security


        return http.build();
    }

    // WebSecurityConfigurerAdapter alternative update code 2/2
    // Set subfiles in the static directory to ignore authentication.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("------------- configure ------------- ");
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

//
//
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(memberService)
//                .passwordEncoder(passwordEncoder());
//
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
