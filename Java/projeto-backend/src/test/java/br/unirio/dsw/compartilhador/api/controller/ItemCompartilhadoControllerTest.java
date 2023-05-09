package br.unirio.dsw.compartilhador.api.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import br.unirio.dsw.compartilhador.api.model.ItemCompartilhado;
import br.unirio.dsw.compartilhador.api.model.TipoItemCompartilhado;
import br.unirio.dsw.compartilhador.api.model.Usuario;
import br.unirio.dsw.compartilhador.api.repository.ItemCompartilhadoRepository;
import br.unirio.dsw.compartilhador.api.repository.UsuarioRepository;
import br.unirio.dsw.compartilhador.api.service.security.LoginForm;

/**
 * Casos de teste para itens compartilhados
 * 
 * @author User
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ItemCompartilhadoControllerTest
{
	@Autowired
	private MockMvc mvc;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ItemCompartilhadoRepository itemCompartilhadoRepository;
	
	private Usuario usuario;
	
	private String token;

	/**
	 * Prepara e loga o usuário antes dos casos de teste
	 */
	@Before
	public void setup() throws Exception
	{
		this.usuario = registraUsuario();
		this.token = realizaLogin();
	}

	/**
	 * Registra um novo usuário
	 */
	private Usuario registraUsuario()
	{
		Usuario usuario = new Usuario();
		usuario.setNome("Fulano");
		usuario.setEmail("fulano@somewhere.com");
		usuario.setSenha(passwordEncoder.encode("Ab#123456"));
		usuarioRepository.save(usuario);
		return usuario;
	}

	/**
	 * Realiza o login do novo usuário antes dos casos de teste
	 */
	private String realizaLogin() throws Exception, JsonProcessingException, UnsupportedEncodingException
	{
		LoginForm form = new LoginForm();
		form.setEmail("fulano@somewhere.com");
		form.setSenha("Ab#123456");
		
		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/auth")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		String contents = resultActions.andReturn().getResponse().getContentAsString();
		DocumentContext jsonContext = JsonPath.parse(contents);
		return jsonContext.read("$.data.token");
	}

	/**
	 * Libera todos os usuários e itens após um caso de teste
	 */
	@After
	public void tearDown()
	{
		usuarioRepository.deleteAll();
		itemCompartilhadoRepository.deleteAll();
	}
	
	/**
	 * Testa o registro de um item compartilhado correto
	 */
	@Test
	public void testNovoItemCompartilhadoCorreto() throws Exception 
	{
		NovoItemCompartilhadoForm form = new NovoItemCompartilhadoForm();
		form.setNome("Item #1");
		form.setDescricao("Descricao Item #1");
		form.setTipo("UNICO");

		mvc.perform(MockMvcRequestBuilders.put("/api/item/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	/**
	 * Testa o registro de um item compartilhado sem o usuário logado
	 */
	@Test
	public void testNovoItemCompartilhadoSemUsuarioLogado() throws Exception 
	{
		NovoItemCompartilhadoForm form = new NovoItemCompartilhadoForm();
		form.setNome("Item #1");
		form.setDescricao("Descricao Item #1");
		form.setTipo("UNICO");

		mvc.perform(MockMvcRequestBuilders.put("/api/item/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	/**
	 * Testa o registro de um item compartilhado com nome vazio
	 */
	@Test
	public void testNovoItemCompartilhadoNomeVazio() throws Exception 
	{
		NovoItemCompartilhadoForm form = new NovoItemCompartilhadoForm();
		form.setNome("");
		form.setDescricao("Descricao Item #1");
		form.setTipo("UNICO");

		mvc.perform(MockMvcRequestBuilders.put("/api/item/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors", hasItem("O nome do item compartilhado não pode ser vazio.")));
	}
	
	/**
	 * Testa o registro de um item compartilhado com nome muito grande
	 */
	@Test
	public void testNovoItemCompartilhadoNomeGigante() throws Exception 
	{
		NovoItemCompartilhadoForm form = new NovoItemCompartilhadoForm();
		form.setNome("NOME GIGANTE NOME GIGANTE NOME GIGANTE NOME GIGANTE NOME GIGANTE NOME GIGANTE NOME GIGANTE NOME GIGANTE ");
		form.setDescricao("Descricao Item #1");
		form.setTipo("UNICO");

		mvc.perform(MockMvcRequestBuilders.put("/api/item/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors", hasItem("O nome do item compartilhado não pode ter mais do que 80 caracteres.")));
	}
	
	/**
	 * Testa o registro de um item compartilhado com nome duplicado
	 */
	@Test
	public void testNovoItemCompartilhadoNomeDuplicado() throws Exception 
	{
		NovoItemCompartilhadoForm form = new NovoItemCompartilhadoForm();
		form.setNome("Item #1");
		form.setDescricao("Descricao Item #1");
		form.setTipo("UNICO");
		
		ItemCompartilhado item = new ItemCompartilhado();
		item.setUsuario(usuario);
		item.setNome("Item #1");
		item.setDescricao("Descricao Item #1");
		item.setTipo(TipoItemCompartilhado.UNICO);
		itemCompartilhadoRepository.save(item);		

		mvc.perform(MockMvcRequestBuilders.put("/api/item/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors", hasItem("Já existe um item compartilhado registrado com este nome no sistema.")));
	}
	
	/**
	 * Testa o registro de um item compartilhado com descrição em branco
	 */
	@Test
	public void testNovoItemCompartilhadoDescricaoVazia() throws Exception 
	{
		NovoItemCompartilhadoForm form = new NovoItemCompartilhadoForm();
		form.setNome("Item #1");
		form.setDescricao("");
		form.setTipo("UNICO");

		mvc.perform(MockMvcRequestBuilders.put("/api/item/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors", hasItem("A descrição do item compartilhado não pode ser vazia.")));
	}
	
	/**
	 * Testa o registro de um item compartilhado com descrição muito grande
	 */
	@Test
	public void testNovoItemCompartilhadoDescricaoGigante() throws Exception 
	{
		NovoItemCompartilhadoForm form = new NovoItemCompartilhadoForm();
		form.setNome("Item #1");
		form.setDescricao("DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA DESCRICAO MEGA");
		form.setTipo("UNICO");

		mvc.perform(MockMvcRequestBuilders.put("/api/item/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors", hasItem("A descrição do item compartilhado não pode ter mais do que 255 caracteres.")));
	}

	/**
	 * Testa o registro de um item compartilhado com tipo inválido
	 */
	@Test
	public void testNovoItemCompartilhadoTipoInvalido() throws Exception 
	{
		NovoItemCompartilhadoForm form = new NovoItemCompartilhadoForm();
		form.setNome("Item #1");
		form.setDescricao("Descricao Item #1");
		form.setTipo("NAO UNICO");

		mvc.perform(MockMvcRequestBuilders.put("/api/item/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors", hasItem("O tipo do item compartilhado deve ser 'único' ou 'múltiplo'.")));
	}
}