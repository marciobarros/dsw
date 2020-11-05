package br.unirio.dsw.compartilhador.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.unirio.dsw.compartilhador.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{
	@Transactional(readOnly=true)
	Usuario findByEmail(String email);

	@Transactional(readOnly=true)
	Usuario findByEmailAndSenha(String email, String senha);
}