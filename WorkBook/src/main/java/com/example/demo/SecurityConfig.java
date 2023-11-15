

package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.example.demo.login.RestMatcher;



@Configuration
@EnableWebSecurity

public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Autowired
	private DataSource dataSource;
	
	private static final String USER_SQL = "SELECT"
			+" email,"
			+" password,"
			+" true"
			+" FROM"
			+" m_user"
			+" WHERE"
			+" email = ?";
	
	private static final String ROLE_SQL = "SELECT"
			+" email,"
			+" role"
			+" FROM"
			+" m_user"
			+" WHERE"
			+" email = ?";
			
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests( authorize -> authorize
				.requestMatchers("/webjars/**").permitAll()
				.requestMatchers("/css/**").permitAll()
				.requestMatchers("/login/**").permitAll()
				.requestMatchers("/signup/**").permitAll()
				.requestMatchers("/rest/**").permitAll()
				.requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated());
		
		
	    http.formLogin( login -> login
	    	// ログイン処理のURL
	        .loginPage("/login")
	        .loginProcessingUrl("/login")
	        // ログイン失敗時の遷移先URL
	        .failureUrl("/login")
	        // usernameのパラメータ名
	        .usernameParameter("email")
	        // passwordのパラメータ名
	        .passwordParameter("password")
	        .defaultSuccessUrl("/home",true)
	        );

    // ログアウト処理の設定
	    http.logout( logout -> logout
	    	// ログアウト処理のURL
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	        .logoutUrl("/logout")
	        // ログアウト成功時の遷移先URL
	        .logoutSuccessUrl("/login")
	        // ログアウト時のセッション破棄を有効化
	        .invalidateHttpSession(true));
    
	    RequestMatcher csrfMatcher = new RestMatcher("/rest/**");
	    
	    http.csrf( csrf -> csrf
	    		.requireCsrfProtectionMatcher(csrfMatcher));
	    
//		http.csrf( csrf -> csrf
//			.disable());
	    
		
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
	    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
	    manager.setUsersByUsernameQuery(USER_SQL);
	    manager.setAuthoritiesByUsernameQuery(ROLE_SQL);
	    return manager;
	}	

  
}

