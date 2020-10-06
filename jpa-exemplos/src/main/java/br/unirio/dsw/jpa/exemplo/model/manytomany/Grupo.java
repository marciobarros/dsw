package br.unirio.dsw.jpa.exemplo.model.manytomany;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Grupo
{
	private Long id;
	private String nome;
	private String permissoes;

	public Grupo()
	{
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getPermissoes()
	{
		return permissoes;
	}

	public void setPermissoes(String permissoes)
	{
		this.permissoes = permissoes;
	}
}