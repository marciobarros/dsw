package br.unirio.dsw.compartilhador.api.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.unirio.dsw.compartilhador.api.model.Usuario;
import br.unirio.dsw.compartilhador.api.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception 
	{
	    return super.authenticationManagerBean();
	}
	
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
	{
		authenticationManagerBuilder
			.authenticationProvider(new AuthenticationService(usuarioRepository, passwordEncoder()));
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder(10);
	}

	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception
	{
		return new JwtAuthenticationTokenFilter();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception
	{
		httpSecurity.cors()
					.and()
					.csrf().disable()
					.exceptionHandling()
					.authenticationEntryPoint(unauthorizedHandler)
					.and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.authorizeRequests()
						.antMatchers("/auth/**", "/api/usuario/novo", "/api/usuario/esqueci", "/api/usuario/reset", "/configuration/security", "/webjars/**").permitAll()
						.anyRequest().authenticated();
		
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		httpSecurity.headers().cacheControl();
	}
	
	/**
	 * Classe responsável pela autenticação de usuários
	 * 
	 * @author marciobarros
	 */
	private class AuthenticationService implements AuthenticationProvider
	{
		private UsuarioRepository usuarioRepositorio;
		
		private PasswordEncoder passwordEncoder;
		
		public AuthenticationService(UsuarioRepository usuarioRepositorio, PasswordEncoder passwordEncoder)
		{
			this.usuarioRepositorio = usuarioRepositorio;
			this.passwordEncoder = passwordEncoder;
		}

		@Override
		public Authentication authenticate(Authentication authentication) throws AuthenticationException
		{
			Usuario usuario = usuarioRepositorio.findByEmail(authentication.getName());
			
			if (usuario == null)
				throw new BadCredentialsException("Credenciais não reconhecidas.");
				
			if (!passwordEncoder.matches(authentication.getCredentials().toString(), usuario.getSenha()))
			{
				usuario.setFalhasLogin(usuario.getFalhasLogin() + 1);
				
				if (usuario.getFalhasLogin() >= 3)
					usuario.setBloqueado(true);
				
				usuarioRepositorio.save(usuario);
				throw new BadCredentialsException("Credenciais não reconhecidas.");
			}
			
			if (usuario.isBloqueado())
				throw new LockedException("A conta do usuário está bloqueada.");

			usuario.setFalhasLogin(0);
			usuarioRepositorio.save(usuario);

	        return new UsernamePasswordAuthenticationToken(authentication.getName(), null, null);
		}

		@Override
		public boolean supports(Class<?> authentication) 
		{
		    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
		}
	}
}