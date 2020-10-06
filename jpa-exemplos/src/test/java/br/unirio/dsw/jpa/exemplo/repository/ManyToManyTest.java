package br.unirio.dsw.jpa.exemplo.repository;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.unirio.dsw.jpa.exemplo.model.manytomany.Grupo;
import br.unirio.dsw.jpa.exemplo.model.manytomany.Usuario;
import br.unirio.dsw.jpa.exemplo.repository.manytomany.GrupoRepository;
import br.unirio.dsw.jpa.exemplo.repository.manytomany.UsuarioRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManyToManyTest
{
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Before
	public void setUp() throws Exception
	{
		Grupo grupoAdministradores = new Grupo();
		grupoAdministradores.setNome("admin");
		this.grupoRepository.save(grupoAdministradores);
		
		Grupo grupoUsuarios = new Grupo();
		grupoUsuarios.setNome("user");
		this.grupoRepository.save(grupoUsuarios);

		Usuario mick = new Usuario();
		mick.setNome("Mick J.");
		mick.getGrupos().add(grupoAdministradores);
		mick.getGrupos().add(grupoUsuarios);
		this.usuarioRepository.save(mick);

		Usuario keith = new Usuario();
		keith.setNome("Keith R.");
		keith.getGrupos().add(grupoAdministradores);
		keith.getGrupos().add(grupoUsuarios);
		this.usuarioRepository.save(keith);

		Usuario charlie = new Usuario();
		charlie.setNome("Charlie W.");
		charlie.getGrupos().add(grupoUsuarios);
		this.usuarioRepository.save(charlie);

		Usuario ronnie = new Usuario();
		ronnie.setNome("Ronnie W.");
		ronnie.getGrupos().add(grupoUsuarios);
		this.usuarioRepository.save(ronnie);
	}
	
	@After
	public void tearDown() throws Exception
	{
		this.usuarioRepository.deleteAll();
		this.grupoRepository.deleteAll();
	}

	@Test
	public void testMick()
	{
		Usuario mick = usuarioRepository.findByNome("Mick J.");
		assertEquals("Mick J.", mick.getNome());
		assertEquals(2, mick.getGrupos().size());
	}

	@Test
	public void testKeith()
	{
		Usuario keith = usuarioRepository.findByNome("Keith R.");
		assertEquals("Keith R.", keith.getNome());
		assertEquals(2, keith.getGrupos().size());
	}

	@Test
	public void testCharlie()
	{
		Usuario charlie = usuarioRepository.findByNome("Charlie W.");
		assertEquals(1, charlie.getGrupos().size());
	}

	@Test
	public void testRonnie()
	{
		Usuario ronnie = usuarioRepository.findByNome("Ronnie W.");
		assertEquals(1, ronnie.getGrupos().size());
	}
}