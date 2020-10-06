package br.unirio.dsw.jpa.exemplo.repository.manytomany;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unirio.dsw.jpa.exemplo.model.manytomany.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{
	@Transactional
	Usuario findByNome(String nome);
}