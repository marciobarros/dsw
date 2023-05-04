package br.unirio.dsw.jpa.exemplo.repository.onetoone;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unirio.dsw.jpa.exemplo.model.onetoone.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>
{
	@Transactional
	Pessoa findByNome(String nome);
}