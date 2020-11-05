package br.unirio.dsw.compartilhador.api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.unirio.dsw.compartilhador.api.model.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest
{
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String EMAIL = "fulano@somewhere.com";
	
	private static final String SENHA = "123456";
	
	@Before
	public void setUp() throws Exception
	{
		Usuario usuario = new Usuario();
		usuario.setNome("Teste");
		usuario.setEmail(EMAIL);
		usuario.setSenha(SENHA);
		this.usuarioRepository.save(usuario);
	}
	
	@After
	public void tearDown() throws Exception
	{
		this.usuarioRepository.deleteAll();
	}

	@Test
	public void testFindByEmail()
	{
		Usuario usuario = usuarioRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, usuario.getEmail());
	}

	@Test
	public void testFindByInexistentEmail()
	{
		Usuario usuario = usuarioRepository.findByEmail("teste@somewhere.com");
		assertNull(usuario);
	}

	@Test
	public void testFindByEmailAndPassword()
	{
		Usuario usuario = usuarioRepository.findByEmailAndSenha(EMAIL, SENHA);
		assertEquals(EMAIL, usuario.getEmail());
		assertEquals(SENHA, usuario.getSenha());
	}

	@Test
	public void testFindByEmailAndWrongPassword()
	{
		Usuario usuario = usuarioRepository.findByEmailAndSenha(EMAIL, SENHA + "*");
		assertNull(usuario);
	}
}