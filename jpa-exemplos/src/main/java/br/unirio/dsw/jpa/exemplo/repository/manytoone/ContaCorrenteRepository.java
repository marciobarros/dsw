package br.unirio.dsw.jpa.exemplo.repository.manytoone;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unirio.dsw.jpa.exemplo.model.manytoone.ContaCorrente;

public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long>
{
	@Transactional
	ContaCorrente findByAgenciaAndNumero(String agencia, String numero);
}