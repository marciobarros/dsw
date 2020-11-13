package br.unirio.dsw.compartilhador.api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.unirio.dsw.compartilhador.api.model.ItemCompartilhado;
import br.unirio.dsw.compartilhador.api.model.TipoItemCompartilhado;
import br.unirio.dsw.compartilhador.api.model.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ItemCompartilhadoRepositoryTest
{
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ItemCompartilhadoRepository itemCompartilhadoRepository;
	
	private Usuario fulano;
	
	private Usuario beltrano;
	
	private Usuario ciclano;
	
	private Usuario joao;

	@Before
	public void setUp() throws Exception
	{
		this.fulano = createUser("Fulano", "fulano@somewhere.com");
		this.beltrano = createUser("Beltrano", "beltrano@somewhere.com");
		this.ciclano = createUser("Ciclano", "ciclano@somewhere.com");
		this.joao = createUser("Joao", "joao@somewhere.com");
		
		createSharedItem("Item #1", fulano);
		createSharedItem("Item #2", fulano);
		createSharedItem("Item #3", fulano);
		createSharedItem("Item #4", beltrano);
		createSharedItem("Item #5", ciclano);
	}

	@After
	public final void tearDown()
	{
		this.itemCompartilhadoRepository.deleteAll();
	}

	@Test
	public void testBuscarItemCompartilhadoNome()
	{
		ItemCompartilhado item = this.itemCompartilhadoRepository.findByNome("Item #1");
		assertEquals("Item #1", item.getNome());
	}

	@Test
	public void testBuscarItemCompartilhadoNomeInexistente()
	{
		ItemCompartilhado item = this.itemCompartilhadoRepository.findByNome("Item #X");
		assertNull(item);
	}

	@Test
	public void testBuscarItemsCompartilhadoFulano()
	{
		List<ItemCompartilhado> items = this.itemCompartilhadoRepository.findByUsuarioId(fulano.getId());
		assertEquals(3, items.size());
		assertEquals("Item #1", items.get(0).getNome());
		assertEquals("Item #2", items.get(1).getNome());
		assertEquals("Item #3", items.get(2).getNome());
	}

	@Test
	public void testBuscarItemsCompartilhadoJoao()
	{
		List<ItemCompartilhado> items = this.itemCompartilhadoRepository.findByUsuarioId(joao.getId());
		assertEquals(0, items.size());
	}

	private Usuario createUser(String nome, String email)
	{
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha("123456");
		this.usuarioRepository.save(usuario);
		return usuario;
	}

	private ItemCompartilhado createSharedItem(String nome, Usuario usuario)
	{
		ItemCompartilhado item = new ItemCompartilhado();
		item.setNome(nome);
		item.setDescricao("Descrição do " + nome.toLowerCase());
		item.setUsuario(usuario);
		item.setTipo(TipoItemCompartilhado.UNICO);
		item.setRemovido(false);
		this.itemCompartilhadoRepository.save(item);
		return item;
	}
}