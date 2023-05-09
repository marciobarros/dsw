package br.unirio.dsw.compartilhador.api.repository;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.unirio.dsw.compartilhador.api.model.Compartilhamento;

@Transactional(readOnly = true)
@NamedQueries({
	@NamedQuery(name = "CompartilhamentoRepository.bagaca", query = "SELECT ic FROM ItemCompartilhado ic WHERE ic.usuario.id = :ownerId") 
})
public interface CompartilhamentoRepository extends JpaRepository<Compartilhamento, Long>
{
	List<Compartilhamento> findByUsuarioId(Long usuarioId);

	Page<Compartilhamento> findByUsuarioId(Long usuarioId, Pageable pageable);

	List<Compartilhamento> findByUsuarioIdAndAceito(Long usuarioId, boolean aceito);
}