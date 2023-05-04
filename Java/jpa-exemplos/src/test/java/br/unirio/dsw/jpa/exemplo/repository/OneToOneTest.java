package br.unirio.dsw.jpa.exemplo.repository;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.unirio.dsw.jpa.exemplo.model.onetoone.Endereco;
import br.unirio.dsw.jpa.exemplo.model.onetoone.Pessoa;
import br.unirio.dsw.jpa.exemplo.repository.onetoone.EnderecoRepository;
import br.unirio.dsw.jpa.exemplo.repository.onetoone.PessoaRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OneToOneTest
{
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Before
	public void setUp() throws Exception
	{
		registraJoao();
		registraJose();
		registraMaria();
	}

	private void registraJoao()
	{
		Endereco enderecoJoao = new Endereco();
		enderecoJoao.setLocalidade("Rua do Joao");
		enderecoJoao.setNumero("10");
		enderecoJoao.setComplemento("");
		enderecoJoao.setMunicipio("Rio de Janeiro");
		enderecoJoao.setEstado("RJ");
		enderecoJoao.setCep("22222-222");
		this.enderecoRepository.save(enderecoJoao);
				
		Pessoa joao = new Pessoa();
		joao.setNome("Joao");
		joao.setEndereco(enderecoJoao);
		joao.setTelefone("21 2222-2222");
		this.pessoaRepository.save(joao);
	}

	private void registraJose()
	{
		Endereco enderecoJose = new Endereco();
		enderecoJose.setLocalidade("Rua do Jose");
		enderecoJose.setNumero("100");
		enderecoJose.setComplemento("");
		enderecoJose.setMunicipio("Rio de Janeiro");
		enderecoJose.setEstado("RJ");
		enderecoJose.setCep("33333-333");
		this.enderecoRepository.save(enderecoJose);
				
		Pessoa jose = new Pessoa();
		jose.setNome("Jose");
		jose.setEndereco(enderecoJose);
		jose.setTelefone("21 3333-3333");
		this.pessoaRepository.save(jose);
	}

	private void registraMaria()
	{
		Endereco enderecoMaria = new Endereco();
		enderecoMaria.setLocalidade("Rua da Maria");
		enderecoMaria.setNumero("1000");
		enderecoMaria.setComplemento("");
		enderecoMaria.setMunicipio("Rio de Janeiro");
		enderecoMaria.setEstado("RJ");
		enderecoMaria.setCep("44444-444");
		this.enderecoRepository.save(enderecoMaria);
				
		Pessoa maria = new Pessoa();
		maria.setNome("Maria");
		maria.setEndereco(enderecoMaria);
		maria.setTelefone("21 4444-4444");
		this.pessoaRepository.save(maria);
	}
	
	@After
	public void tearDown() throws Exception
	{
		this.pessoaRepository.deleteAll();
	}

	@Test
	public void testFindByName()
	{
		Pessoa pessoa = pessoaRepository.findByNome("Joao");
		assertEquals("Joao", pessoa.getNome());
		assertEquals("21 2222-2222", pessoa.getTelefone());
		assertEquals("Rua do Joao", pessoa.getEndereco().getLocalidade());
		assertEquals("22222-222", pessoa.getEndereco().getCep());

		pessoa = pessoaRepository.findByNome("Jose");
		assertEquals("Jose", pessoa.getNome());
		assertEquals("21 3333-3333", pessoa.getTelefone());
		assertEquals("Rua do Jose", pessoa.getEndereco().getLocalidade());
		assertEquals("33333-333", pessoa.getEndereco().getCep());

		pessoa = pessoaRepository.findByNome("Maria");
		assertEquals("Maria", pessoa.getNome());
		assertEquals("21 4444-4444", pessoa.getTelefone());
		assertEquals("Rua da Maria", pessoa.getEndereco().getLocalidade());
		assertEquals("44444-444", pessoa.getEndereco().getCep());
	}
}