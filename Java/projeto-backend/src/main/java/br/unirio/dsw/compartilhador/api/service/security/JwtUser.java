package br.unirio.dsw.compartilhador.api.service.security;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

/**
 * Classe que representa um usuário no JWT
 * 
 * @author User
 */
public class JwtUser implements UserDetails
{
	private static final long serialVersionUID = -268046329085485932L;

	private @Getter Long id;
	private String username;
	private String password;
	private @Getter Date lastLoginDate;
	private Collection<? extends GrantedAuthority> authorities;

	/**
	 * Inicializa o usuário com as suas informações
	 */
	public JwtUser(Long id, String username, String password, Date lastLoginDate, Collection<? extends GrantedAuthority> authorities)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.lastLoginDate = lastLoginDate;
		this.authorities = authorities;
	}

	/**
	 * Retorna o login do usuário, que no caso é o seu e-mail
	 */
	@Override
	public String getUsername()
	{
		return username;
	}

	/**
	 * Verifica se a conta do usuário é válida
	 */
	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	/**
	 * Verifica se as credenciais do usuário são válidas
	 */
	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	/**
	 * Verifica se a conta do usuário está bloqueada
	 */
	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	/**
	 * Retorna a senha do usuário
	 */
	@Override
	public String getPassword()
	{
		return password;
	}

	/**
	 * Retorna os papéis cumpridos pelo usuário
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return authorities;
	}

	/**
	 * Verifica se o usuário está ativo
	 */
	@Override
	public boolean isEnabled()
	{
		return true;
	}
}