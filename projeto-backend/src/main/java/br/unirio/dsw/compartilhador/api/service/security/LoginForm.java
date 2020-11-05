package br.unirio.dsw.compartilhador.api.service.security;

import lombok.Getter;
import lombok.Setter;

/**
 * Class that represents a login form
 * 
 * @author User
 */
public class LoginForm
{
	private @Getter @Setter String email;
	private @Getter @Setter String senha;
}