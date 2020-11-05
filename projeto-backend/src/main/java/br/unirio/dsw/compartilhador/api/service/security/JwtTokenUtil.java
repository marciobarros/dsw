package br.unirio.dsw.compartilhador.api.service.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil
{
	static final String CLAIM_KEY_USERNAME = "sub";
	
	static final String CLAIM_KEY_ROLE = "role";
	
	static final String CLAIM_KEY_AUDIENCE = "audience";
	
	static final String CLAIM_KEY_CREATED = "created";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	/**
	 * Obtém o username (email) contido no token JWT
	 */
	public String getUsernameFromToken(String token)
	{
		try
		{
			Claims claims = getClaimsFromToken(token);
			return claims.getSubject();
		} 
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Retorna a data de expiração de um token JWT
	 */
	public Date getExpirationDateFromToken(String token)
	{
		try
		{
			Claims claims = getClaimsFromToken(token);
			return claims.getExpiration();
		} 
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Cria um novo token (refresh)
	 */
	public String refreshToken(String token)
	{
		try
		{
			Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			return gerarToken(claims);
		} 
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Verifica e retorna se um token JWT é válido
	 */
	public boolean tokenValido(String token)
	{
		return !tokenExpirado(token);
	}

	/**
	 * Retorna um novo token JWT com base nos dados do usuários
	 */
	public String obterToken(String userName, List<GrantedAuthority> authorities)
	{
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userName);
		
		for (GrantedAuthority authority : authorities)
			claims.put(CLAIM_KEY_ROLE, authority.getAuthority());
		
		claims.put(CLAIM_KEY_CREATED, new Date());
		return gerarToken(claims);
	}

	/**
	 * Realiza o parse do token JWT para extrair as informações contidas no corpo dele
	 */
	private Claims getClaimsFromToken(String token)
	{
		Claims claims;
		
		try
		{
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} 
		catch (Exception e)
		{
			claims = null;
		}
		
		return claims;
	}

	/**
	 * Retorna a data de expiração com base na data atual
	 */
	private Date gerarDataExpiracao()
	{
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	/**
	 * Verifica se um token JTW está expirado
	 */
	private boolean tokenExpirado(String token)
	{
		Date dataExpiracao = this.getExpirationDateFromToken(token);

		if (dataExpiracao == null)
			return false;

		return dataExpiracao.before(new Date());
	}

	/**
	 * Gera um novo token JWT contendo os dados (claims) fornecidos
	 */
	private String gerarToken(Map<String, Object> claims)
	{
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(gerarDataExpiracao())
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
}