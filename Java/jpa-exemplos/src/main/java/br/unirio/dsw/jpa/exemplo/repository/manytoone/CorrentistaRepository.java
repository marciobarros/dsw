package br.unirio.dsw.jpa.exemplo.repository.manytoone;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unirio.dsw.jpa.exemplo.model.manytoone.Correntista;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long>
{
	@Transactional
	Correntista findByNome(String nome);
}