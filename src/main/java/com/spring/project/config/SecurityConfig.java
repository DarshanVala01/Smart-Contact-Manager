package com.spring.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	
	@Autowired
	public CustomAuthSuccessHandler successHandler;
	
		@Bean
		 UserDetailsService getUserDetailsService() {
			return new UserDetailsServiceImpl();
		}
		
		@Bean
		 BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		 DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
			daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
			daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
			
			return daoAuthenticationProvider;
		}
		
		
		 @Bean
		    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
				return configuration.getAuthenticationManager();
			}
	
		 
		// Configure method
		
		@Bean
		SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		
			httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .authenticated()
                    .requestMatchers("/user/**")
                    .authenticated()
                    .requestMatchers("/**")
                    .permitAll()
                    )
            .formLogin(login -> login
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler(successHandler)
                    .permitAll())
            .logout(logout -> logout                                                
            		.logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)                                
                );
				
	return httpSecurity.build();
	
				
	}
		

	
}
