package br.unirio.dsw.compartilhador.api.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.unirio.dsw.compartilhador.api.model.Usuario;
import br.unirio.dsw.compartilhador.api.repository.UsuarioRepository;

/**
 * Classe para testar a criação de novos usuários usando um mock do banco de dados
 * 
 * @author User
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerTest
{
	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsuarioRepository usuarioRepository;

	/**
	 * Testa a criação de um usuário válido
	 */
	@Test
	public void testCriarNovoUsuarioValido() throws Exception 
	{
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setNome("Fulano");
		form.setEmail("fulano@somewhere.com");
		form.setSenha("Ab#123456");
		form.setSenhaRepetida("Ab#123456");

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	/**
	 * Testa a criação de um usuário com o e-mail duplicado
	 */
	@Test
	public void testCriarNovoUsuarioEmailDuplicado() throws Exception 
	{
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setNome("Fulano");
		form.setEmail("fulano@somewhere.com");
		form.setSenha("Ab#123456");
		form.setSenhaRepetida("Ab#123456");
		
		Usuario usuario = new Usuario();
		usuario.setNome("Fulano");
		usuario.setEmail("fulano@somewhere.com");

		BDDMockito.given(this.usuarioRepository.findByEmail("fulano@somewhere.com")).willReturn(usuario);

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Já existe um usuário registrado com este e-mail.")));
	}

	/**
	 * Testa a criação de um usuário sem nome
	 */
	@Test
	public void testCriarNovoUsuarioSemNome() throws Exception 
	{
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setNome("");
		form.setEmail("fulano@somewhere.com");
		form.setSenha("Ab#123456");
		form.setSenhaRepetida("Ab#123456");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("O nome do usuário não pode ser vazio.")));
	}

	/**
	 * Testa a criação de um usuário sem e-mail
	 */
	@Test
	public void testCriarNovoUsuarioSemEmail() throws Exception 
	{
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setNome("Fulano");
		form.setEmail("");
		form.setSenha("Ab#123456");
		form.setSenhaRepetida("Ab#123456");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("O e-mail do usuário não está em um formato adequado.")));
	}

	/**
	 * Testa a criação de um usuário com e-mail inválido
	 */
	@Test
	public void testCriarNovoUsuarioEmailInvalido() throws Exception 
	{
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setNome("Fulano");
		form.setEmail("fulano");
		form.setSenha("Ab#123456");
		form.setSenhaRepetida("Ab#123456");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("O e-mail do usuário não está em um formato adequado.")));
	}

	/**
	 * Testa a criação de um usuário sem senha
	 */
	@Test
	public void testCriarNovoUsuarioSenhaVazia() throws Exception 
	{
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setNome("Fulano");
		form.setEmail("fulano@somewhere.com");
		form.setSenha("");
		form.setSenhaRepetida("");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("A senha do usuário não é válida.")));
	}

	/**
	 * Testa a criação de um usuário com senha trivial
	 */
	@Test
	public void testCriarNovoUsuarioSenhaTrivial() throws Exception 
	{
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setNome("Fulano");
		form.setEmail("fulano@somewhere.com");
		form.setSenha("123456");
		form.setSenhaRepetida("123456");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("A senha do usuário não é válida.")));
	}

	/**
	 * Testa a criação de um usuário com a senha repetida diferente da original
	 */
	@Test
	public void testCriarNovoUsuarioSenhaRepetidaDiferente() throws Exception 
	{
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setNome("Fulano");
		form.setEmail("fulano@somewhere.com");
		form.setSenha("Ab#123456");
		form.setSenhaRepetida("Ba#123456");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/novo")
				.content(new ObjectMapper().writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("A confirmação de senha está diferente da senha.")));
	}
}