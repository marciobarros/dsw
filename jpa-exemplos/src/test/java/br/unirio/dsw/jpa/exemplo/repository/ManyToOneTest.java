package br.unirio.dsw.jpa.exemplo.repository;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.unirio.dsw.jpa.exemplo.model.manytoone.ContaCorrente;
import br.unirio.dsw.jpa.exemplo.model.manytoone.Correntista;
import br.unirio.dsw.jpa.exemplo.repository.manytoone.ContaCorrenteRepository;
import br.unirio.dsw.jpa.exemplo.repository.manytoone.CorrentistaRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManyToOneTest
{
	@Autowired
	private CorrentistaRepository correntistaRepository;
	
	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;
	
	@Before
	public void setUp() throws Exception
	{
		registraContasJoao();
		registraContasMaria();
	}

	private void registraContasJoao()
	{
		Correntista joao = new Correntista();
		joao.setNome("Joao");
		this.correntistaRepository.save(joao);

		ContaCorrente primeiraContaJoao = new ContaCorrente();
		primeiraContaJoao.setBanco("Banco do Joao");
		primeiraContaJoao.setAgencia("0001-1");
		primeiraContaJoao.setNumero("00.001-1");
		primeiraContaJoao.setCorrentista(joao);
		this.contaCorrenteRepository.save(primeiraContaJoao);

		ContaCorrente segundaContaJoao = new ContaCorrente();
		segundaContaJoao.setBanco("Banco do Joao");
		segundaContaJoao.setAgencia("0002-2");
		segundaContaJoao.setNumero("00.002-2");
		segundaContaJoao.setCorrentista(joao);
		this.contaCorrenteRepository.save(segundaContaJoao);
				
		ContaCorrente terceiraContaJoao = new ContaCorrente();
		terceiraContaJoao.setBanco("Banco do Joao");
		terceiraContaJoao.setAgencia("0003-3");
		terceiraContaJoao.setNumero("00.003-3");
		terceiraContaJoao.setCorrentista(joao);
		this.contaCorrenteRepository.save(terceiraContaJoao);
	}

	private void registraContasMaria()
	{
		Correntista maria = new Correntista();
		maria.setNome("Maria");
		this.correntistaRepository.save(maria);

		ContaCorrente primeiraContaMaria = new ContaCorrente();
		primeiraContaMaria.setBanco("Banco da Maria");
		primeiraContaMaria.setAgencia("1001-1");
		primeiraContaMaria.setNumero("10.001-1");
		primeiraContaMaria.setCorrentista(maria);
		this.contaCorrenteRepository.save(primeiraContaMaria);

		ContaCorrente segundaContaMaria = new ContaCorrente();
		segundaContaMaria.setBanco("Banco da Maria");
		segundaContaMaria.setAgencia("1002-2");
		segundaContaMaria.setNumero("10.002-2");
		segundaContaMaria.setCorrentista(maria);
		this.contaCorrenteRepository.save(segundaContaMaria);
	}
	
	@After
	public void tearDown() throws Exception
	{
		this.contaCorrenteRepository.deleteAll();
		this.correntistaRepository.deleteAll();
	}

	@Test
	public void testJoao()
	{
		ContaCorrente conta1 = contaCorrenteRepository.findByAgenciaAndNumero("0001-1", "00.001-1");
		assertEquals("Joao", conta1.getCorrentista().getNome());
		assertEquals("Banco do Joao", conta1.getBanco());
		assertEquals("0001-1", conta1.getAgencia());
		assertEquals("00.001-1", conta1.getNumero());

		ContaCorrente conta2 = contaCorrenteRepository.findByAgenciaAndNumero("0002-2", "00.002-2");
		assertEquals("Joao", conta2.getCorrentista().getNome());
		assertEquals("Banco do Joao", conta2.getBanco());
		assertEquals("0002-2", conta2.getAgencia());
		assertEquals("00.002-2", conta2.getNumero());

		ContaCorrente conta3 = contaCorrenteRepository.findByAgenciaAndNumero("0003-3", "00.003-3");
		assertEquals("Joao", conta3.getCorrentista().getNome());
		assertEquals("Banco do Joao", conta3.getBanco());
		assertEquals("0003-3", conta3.getAgencia());
		assertEquals("00.003-3", conta3.getNumero());
	}

	@Test
	public void testMaria()
	{
		ContaCorrente conta1 = contaCorrenteRepository.findByAgenciaAndNumero("1001-1", "10.001-1");
		assertEquals("Maria", conta1.getCorrentista().getNome());
		assertEquals("Banco da Maria", conta1.getBanco());
		assertEquals("1001-1", conta1.getAgencia());
		assertEquals("10.001-1", conta1.getNumero());

		ContaCorrente conta2 = contaCorrenteRepository.findByAgenciaAndNumero("1002-2", "10.002-2");
		assertEquals("Maria", conta2.getCorrentista().getNome());
		assertEquals("Banco da Maria", conta2.getBanco());
		assertEquals("1002-2", conta2.getAgencia());
		assertEquals("10.002-2", conta2.getNumero());
	}
}