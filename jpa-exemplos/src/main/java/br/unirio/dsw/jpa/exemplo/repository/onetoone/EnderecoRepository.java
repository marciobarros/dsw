package br.unirio.dsw.jpa.exemplo.repository.onetoone;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unirio.dsw.jpa.exemplo.model.onetoone.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>
{
}