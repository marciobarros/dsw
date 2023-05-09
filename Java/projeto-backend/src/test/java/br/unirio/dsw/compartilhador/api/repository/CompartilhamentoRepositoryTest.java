package br.unirio.dsw.compartilhador.api.repository;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.unirio.dsw.compartilhador.api.model.Compartilhamento;
import br.unirio.dsw.compartilhador.api.model.ItemCompartilhado;
import br.unirio.dsw.compartilhador.api.model.TipoItemCompartilhado;
import br.unirio.dsw.compartilhador.api.model.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CompartilhamentoRepositoryTest
{
	// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ItemCompartilhadoRepository itemCompartilhadoRepository;

	@Autowired
	private CompartilhamentoRepository compartilhamentoRepository;
	
	private Usuario fulano;
	
	private Usuario beltrano;
	
	private Usuario ciclano;
	
	private ItemCompartilhado itemCompartilhado1;
	
	private ItemCompartilhado itemCompartilhado2;
	
	@Before
	public void setUp() throws Exception
	{
		this.fulano = createUser("Fulano", "fulano@somewhere.com");
		this.beltrano = createUser("Beltrano", "beltrano@somewhere.com");
		this.ciclano = createUser("Ciclano", "ciclano@somewhere.com");
		createUser("Joao", "joao@somewhere.com");
		
		this.itemCompartilhado1 = createSharedItem("Item #1", fulano);
		this.itemCompartilhado2 = createSharedItem("Item #2", fulano);
		createSharedItem("Item #3", fulano);
		createSharedItem("Item #4", beltrano);
		createSharedItem("Item #5", ciclano);
		
		LocalDate dataInicio = LocalDate.of(2020, 2, 1);
		LocalDate dataTermino = LocalDate.of(2020, 2, 28);
		
		createSharing(itemCompartilhado1, beltrano, dataInicio, dataTermino, true);
		createSharing(itemCompartilhado1, ciclano, dataInicio, dataTermino, false);
		createSharing(itemCompartilhado2, ciclano, dataInicio, dataTermino, false);
	}

	@After
	public final void tearDown()
	{
		this.itemCompartilhadoRepository.deleteAll();
	}

	@Test
	public void testBuscarCompartilhamentoUnicoUsuario()
	{
		List<Compartilhamento> compartilhamentos = compartilhamentoRepository.findByUsuarioId(beltrano.getId());
		assertEquals(1, compartilhamentos.size());
	}

	@Test
	public void testBuscarCompartilhamentoUsuarioAceito()
	{
		List<Compartilhamento> compartilhamentos = compartilhamentoRepository.findByUsuarioIdAndAceito(beltrano.getId(), true);
		assertEquals(1, compartilhamentos.size());
		assertEquals("Item #1", compartilhamentos.get(0).getItem().getNome());
	}

	@Test
	public void testBuscarCompartilhamentoUsuarioNaoAceito()
	{
		List<Compartilhamento> compartilhamentos = compartilhamentoRepository.findByUsuarioIdAndAceito(beltrano.getId(), false);
		assertEquals(0, compartilhamentos.size());
	}

	@Test
	public void testBuscarCompartilhamentoMultiploUsuario()
	{
		List<Compartilhamento> compartilhamentos = compartilhamentoRepository.findByUsuarioId(ciclano.getId());
		assertEquals(2, compartilhamentos.size());
		assertEquals("Item #1", compartilhamentos.get(0).getItem().getNome());
		assertEquals("Item #2", compartilhamentos.get(1).getItem().getNome());
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

	private Compartilhamento createSharing(ItemCompartilhado item, Usuario usuario, LocalDate dataInicio, LocalDate dataTermino, boolean aceito)
	{
		Compartilhamento compartilhamento = new Compartilhamento();
		compartilhamento.setUsuario(usuario);
		compartilhamento.setItem(item);
		compartilhamento.setDataInicio(dataInicio);
		compartilhamento.setDataTermino(dataTermino);
		compartilhamento.setAceito(aceito);
		compartilhamento.setRejeitado(false);
		compartilhamento.setCanceladoDono(false);
		compartilhamento.setCanceladoUsuario(false);
		this.compartilhamentoRepository.save(compartilhamento);
		return compartilhamento;
	}
}