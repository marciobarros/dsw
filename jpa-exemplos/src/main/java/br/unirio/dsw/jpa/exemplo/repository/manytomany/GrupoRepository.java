package br.unirio.dsw.jpa.exemplo.repository.manytomany;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unirio.dsw.jpa.exemplo.model.manytomany.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long>
{
	@Transactional
	Grupo findByNome(String nome);
}