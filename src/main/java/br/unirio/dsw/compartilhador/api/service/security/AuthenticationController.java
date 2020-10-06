package br.unirio.dsw.compartilhador.api.service.security;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unirio.dsw.compartilhador.api.model.Usuario;
import br.unirio.dsw.compartilhador.api.repository.UsuarioRepository;
import br.unirio.dsw.compartilhador.api.utils.ValidationUtils;
import br.unirio.dsw.compartilhador.api.utils.spring.ControllerResponse;
import br.unirio.dsw.compartilhador.api.utils.spring.ResponseData;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController
{
	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

	private static final String TOKEN_HEADER = "Authorization";
	
	private static final String BEARER_PREFIX = "Bearer ";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UsuarioRepository usuarioRepositorio;

	/**
	 * Cria um novo token JWT após a validação de e-mail e senha
	 */
	@PostMapping
	public ResponseEntity<ResponseData> gerarTokenJwt(@RequestBody LoginForm authenticationDto, BindingResult result) throws AuthenticationException
	{
		if (!ValidationUtils.validEmail(authenticationDto.getEmail()))
			return ControllerResponse.fail("Email inválido.");

		if (!ValidationUtils.validPassword(authenticationDto.getSenha()))
			return ControllerResponse.fail("Senha inválida.");

		try
		{
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getSenha());
			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch(Exception e)
		{
			return ControllerResponse.fail("Credenciais inválidas.");
		}

		Usuario usuario = usuarioRepositorio.findByEmail(authenticationDto.getEmail());
		
		if (usuario == null)
			return ControllerResponse.fail("Credenciais inválidas.");
		
		String sToken = jwtTokenUtil.obterToken(usuario.getEmail(), usuario.getAutorizacoes());
		return ControllerResponse.success(new TokenForm(sToken));
	}

	/**
	 * Gera um novo token com uma nova data de expiração
	 */
	@PostMapping(value = "/refresh")
	public ResponseEntity<ResponseData> gerarRefreshTokenJwt(HttpServletRequest request)
	{
		log.info("Gerando refresh token JWT.");
		String token = request.getHeader(TOKEN_HEADER);

		if (token != null && token.startsWith(BEARER_PREFIX))
			token = token.substring(7);

		if (token == null)
			return ControllerResponse.fail("Token não informado.");

		if (!jwtTokenUtil.tokenValido(token))
			return ControllerResponse.fail("Token inválido ou expirado.");

		String refreshedToken = jwtTokenUtil.refreshToken(token);
		return ControllerResponse.success(new TokenForm(refreshedToken));
	}
}

/**
 * Class that represents a token sent to the user
 */
class TokenForm
{
	private @Getter @Setter String token;

	public TokenForm(String token)
	{
		this.token = token;
	}
}