package br.unirio.dsw.compartilhador.api.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import br.unirio.dsw.compartilhador.api.model.Usuario;
import br.unirio.dsw.compartilhador.api.repository.UsuarioRepository;
import br.unirio.dsw.compartilhador.api.service.security.LoginForm;

/**
 * Classe que testa a autenticação de usuários com um banco de dados em memória
 * 
 * @author User
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest
{
	@Autowired
	private MockMvc mvc;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Cria um novo usuário no banco de dados antes de cada caso de teste 
	 */
	@Before
	public void setup()
	{
		Usuario usuario = new Usuario();
		usuario.setNome("Fulano");
		usuario.setEmail("fulano@somewhere.com");
		usuario.setSenha(passwordEncoder.encode("Ab#123456"));
		usuarioRepository.save(usuario);
	}

	/**
	 * Remove todos os usuários do banco de dados após cada caso de teste
	 */
	@After
	public void tearDown()
	{
		usuarioRepository.deleteAll();
	}

	/**
	 * Testa um login com credenciais válidas
	 */
	@Test
	public void testLoginValido() throws Exception 
	{
		LoginForm form = new LoginForm();
		form.setEmail("fulano@somewhere.com");
		form.setSenha("Ab#123456");
		
		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/auth")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.token").isString());
		
		String contents = resultActions.andReturn().getResponse().getContentAsString();
		DocumentContext jsonContext = JsonPath.parse(contents);
		String token = jsonContext.read("$.data.token");
		assertNotNull(token);
	}

	/**
	 * Testa um login com e-mail inválido
	 */
	@Test
	public void testLoginEmailInvalido() throws Exception 
	{
		LoginForm form = new LoginForm();
		form.setEmail("fulanox@somewhere.com");
		form.setSenha("Ab#123456");
		
		mvc.perform(MockMvcRequestBuilders.post("/auth")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Credenciais inválidas.")));
		
		Usuario usuario = usuarioRepository.findByEmail("fulano@somewhere.com");
		assertNotNull(usuario);
		assertEquals(0, usuario.getFalhasLogin());			// Não identificou o usuário		
	}

	/**
	 * Testa um login com senha inválida
	 */
	@Test
	public void testLoginSenhaInvalida() throws Exception 
	{
		LoginForm form = new LoginForm();
		form.setEmail("fulano@somewhere.com");
		form.setSenha("Ab#123456-");
		
		mvc.perform(MockMvcRequestBuilders.post("/auth")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Credenciais inválidas.")));
		
		Usuario usuario = usuarioRepository.findByEmail("fulano@somewhere.com");
		assertNotNull(usuario);
		assertEquals(1, usuario.getFalhasLogin());		
	}

	/**
	 * Testa três logins consecutivos com senha inválida
	 */
	@Test
	public void testLoginSenhaInvalidaTresVezesBloqueia() throws Exception 
	{
		LoginForm form = new LoginForm();
		form.setEmail("fulano@somewhere.com");
		form.setSenha("Ab#123456-");
		
		for (int i = 0; i < 3; i++)
		{
			mvc.perform(MockMvcRequestBuilders.post("/auth")
					.content(new ObjectMapper().writeValueAsString(form))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.errors").isArray())
	                .andExpect(jsonPath("$.errors", hasSize(1)))
	                .andExpect(jsonPath("$.errors", hasItem("Credenciais inválidas.")));
		}
		
		Usuario usuario = usuarioRepository.findByEmail("fulano@somewhere.com");
		assertNotNull(usuario);
		assertEquals(3, usuario.getFalhasLogin());
		assertTrue(usuario.isBloqueado());
	}

	/**
	 * Testa dois logins consecutivos com senha inválida e uma senha correta
	 */
	@Test
	public void testLoginSenhaInvalidaDuasVezesDepoisCorreta() throws Exception 
	{
		LoginForm formErro = new LoginForm();
		formErro.setEmail("fulano@somewhere.com");
		formErro.setSenha("Ab#123456-");
		
		for (int i = 0; i < 2; i++)
		{
			mvc.perform(MockMvcRequestBuilders.post("/auth")
					.content(new ObjectMapper().writeValueAsString(formErro))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.errors").isArray())
	                .andExpect(jsonPath("$.errors", hasSize(1)))
	                .andExpect(jsonPath("$.errors", hasItem("Credenciais inválidas.")));
		}

		LoginForm formCorreto = new LoginForm();
		formCorreto.setEmail("fulano@somewhere.com");
		formCorreto.setSenha("Ab#123456");
		
		mvc.perform(MockMvcRequestBuilders.post("/auth")
				.content(new ObjectMapper().writeValueAsString(formCorreto))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty());

		Usuario usuario = usuarioRepository.findByEmail("fulano@somewhere.com");
		assertNotNull(usuario);
		assertEquals(0, usuario.getFalhasLogin());
		assertFalse(usuario.isBloqueado());
	}
}