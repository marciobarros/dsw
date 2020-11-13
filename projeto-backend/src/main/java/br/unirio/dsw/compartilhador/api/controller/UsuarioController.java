package br.unirio.dsw.compartilhador.api.controller;

import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unirio.dsw.compartilhador.api.model.Usuario;
import br.unirio.dsw.compartilhador.api.repository.UsuarioRepository;
import br.unirio.dsw.compartilhador.api.service.EmailService;
import br.unirio.dsw.compartilhador.api.utils.CryptoUtils;
import br.unirio.dsw.compartilhador.api.utils.ValidationUtils;
import br.unirio.dsw.compartilhador.api.utils.spring.ControllerResponse;
import br.unirio.dsw.compartilhador.api.utils.spring.ResponseData;
import lombok.Data;

/**
 * Controlador com as ações relacionadas a usuários
 * 
 * @author User
 */
@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController
{
	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioRepository usuarioRepositorio;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${app.hostname}")
	private String hostname;

	/**
	 * Ação que registra um novo usuário
	 */
	@PostMapping(value = "/novo")
	public ResponseEntity<ResponseData> novo(@RequestBody UsuarioRegistroForm form, BindingResult result)
	{
		log.info("Criando um novo usuário: {}", form.toString());
		Usuario usuarioEmail = usuarioRepositorio.findByEmail(form.getEmail());

		if (usuarioEmail != null)
			return ControllerResponse.fail("email", "Já existe um usuário registrado com este e-mail.");
		
		if (form.getNome().length() == 0)
			return ControllerResponse.fail("nome", "O nome do usuário não pode ser vazio.");
		
		if (!ValidationUtils.validEmail(form.getEmail()))
			return ControllerResponse.fail("email", "O e-mail do usuário não está em um formato adequado.");

		if (!ValidationUtils.validPassword(form.getSenha()))
			return ControllerResponse.fail("senha", "A senha do usuário não é válida.");
		
		if (!form.getSenha().equals(form.getSenhaRepetida()))
			return ControllerResponse.fail("senhaRepetida", "A confirmação de senha está diferente da senha.");
 
		String encryptedPassword = passwordEncoder.encode(form.getSenha());
		
        Usuario usuario = new Usuario();
        usuario.setNome(form.getNome());
        usuario.setEmail(form.getEmail());
        usuario.setSenha(encryptedPassword);
        usuarioRepositorio.save(usuario);
		return ControllerResponse.success();
	}
	
	/**
	 * Ação que envia um e-mail de recuperação de senha
	 */
	@PostMapping(value = "/esqueci")
    public ResponseEntity<ResponseData> enviaRecuperacaoSenha(@RequestBody EsquecimentoSenhaForm form, BindingResult result) 
	{
		log.info("Pedindo a recuperacao de senha do usuário: {}", form.toString());

		if (form.getEmail().length() == 0)
			return ControllerResponse.fail("email", "O e-mail do usuário não pode ser vazio.");
		
		if (!ValidationUtils.validEmail(form.getEmail()))
			return ControllerResponse.fail("email", "O e-mail do usuário não é válido.");
		
		Usuario usuario = usuarioRepositorio.findByEmail(form.getEmail());

		if (usuario == null)
			return ControllerResponse.fail("email", "Não foi encontrado um usuário com este e-mail.");
		
		usuario.setTokenSenha(CryptoUtils.createToken());
		usuario.setDataTokenSenha(new Date());
		usuarioRepositorio.save(usuario);
		
		String url = hostname + "/#/login/reset?token=" + usuario.getTokenSenha() + "&email=" + usuario.getEmail();		
		String title = "Recuperação de senha";
		
		String contents = "Se você solicitou uma recuperação de senha, clique <a href='" + url + "'>aqui</a>.<br>";
		contents += "Se não tiver pedido a recuperação de senha, relaxa. Só tentaram de dar um golpe!";
		
		emailService.sendToUser(usuario.getNome(), usuario.getEmail(), title, contents);
		
		return ControllerResponse.success();
    }
	
	/**
	 * Ação que troca a senha baseada em reinicialização
	 */
	@PostMapping(value = "/reset")
	public ResponseEntity<ResponseData> reinicializaSenha(@RequestBody RecuperacaoSenhaForm form, BindingResult result, Locale locale)
	{
		log.info("Recuperando a senha do usuário: {}", form.toString());

		if (form.getEmail().length() == 0)
			return ControllerResponse.fail("senha", "O e-mail do usuário não pode ser vazio.");
		
		if (!ValidationUtils.validEmail(form.getEmail()))
			return ControllerResponse.fail("senha", "O e-mail do usuário não é válido.");
		
		if (form.getToken().length() == 0)
			return ControllerResponse.fail("senha", "O token do usuário não é válido.");
		
		Usuario usuario = usuarioRepositorio.findByEmail(form.getEmail());

		if (usuario == null)
			return ControllerResponse.fail("senha", "Não foi encontrado um usuário com este e-mail.");
		
		if (!verificaValidadeTokenLogin(usuario, form.getToken(), 72))
			return ControllerResponse.fail("senha", "O token de troca de senha do usuário está vencido.");
		
		if (!ValidationUtils.validPassword(form.getSenha()))
			return ControllerResponse.fail("senha", "A senha do usuário não é válida.");
		
		if (!form.getSenha().equals(form.getSenhaRepetida()))
			return ControllerResponse.fail("senhaRepetida", "A confirmação de senha está diferente da senha.");
		
		String encryptedPassword = passwordEncoder.encode(form.getSenha());

		usuario.setSenha(encryptedPassword);
        usuarioRepositorio.save(usuario);
		return ControllerResponse.success();
	}
	
	/**
	 * Verifica a validade de um token gerado para troca de senha
	 */
	private boolean verificaValidadeTokenLogin(Usuario usuario, String token, int maximoHoras)
	{
		Date dateToken = usuario.getDataTokenSenha();
		Date dateNow = new Date();
	    double hours = (dateNow.getTime() - dateToken.getTime()) / (60 * 60 * 1000);
		return (hours < maximoHoras);
	}

	/**
	 * Ação que troca a senha do usuário logado
	 */
	@PostMapping(value = "/trocaSenha")
	public ResponseEntity<ResponseData> trocaSenha(@RequestBody TrocaSenhaForm form, BindingResult result, Locale locale)
	{
		log.info("Recuperando a senha do usuário: {}", form.toString());

		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (username == null)
			return ControllerResponse.fail("senhaAntiga", "Não há um usuário logado no sistema.");

        Usuario usuario = usuarioRepositorio.findByEmail(username);

		if (usuario == null)
			return ControllerResponse.fail("senhaAntiga", "Não foi possível recuperar os dados do usuário a partir das credenciais.");
        
		if (!ValidationUtils.validPassword(form.getSenhaAntiga()))
			return ControllerResponse.fail("senhaAntiga", "A senha atual do usuário não é válida.");
		
        if (!passwordEncoder.matches(form.getSenhaAntiga(), usuario.getSenha()))
			return ControllerResponse.fail("senhaAntiga", "A senha atual não está igual à senha registrada no sistema.");
		
		if (!ValidationUtils.validPassword(form.getSenhaNova()))
			return ControllerResponse.fail("senhaNova", "A nova senha do usuário não é válida.");
		
		if (!form.getSenhaNova().equals(form.getSenhaNovaRepetida()))
			return ControllerResponse.fail("senhaNovaRepetida", "A confirmação de senha está diferente da senha.");
 
		String encryptedPassword = passwordEncoder.encode(form.getSenhaNova());
		usuario.setSenha(encryptedPassword);
        usuarioRepositorio.save(usuario);
		return ControllerResponse.success();
	}
}

/**
 * Formulário para registro de um novo usuário
 * 
 * @author User
 */
@Data class UsuarioRegistroForm
{
	private String nome;
	
	private String email;
	
	private String senha;
	
	private String senhaRepetida;
}

/**
 * Formulário para recuperação de senha de usuário não logado
 * 
 * @author User
 */
@Data class EsquecimentoSenhaForm
{
	private String email;
}

/**
 * Formulário para o usuário não logado recuperar a sua senha
 * 
 * @author User
 */
@Data class RecuperacaoSenhaForm
{
	private String email;
	
	private String token;
	
	private String senha;

	private String senhaRepetida;
}

/**
 * Formulário para troca de senha por usuário logado
 * 
 * @author User
 */
@Data class TrocaSenhaForm
{
	private String senhaAntiga;

	private String senhaNova;

	private String senhaNovaRepetida;
}