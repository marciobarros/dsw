package br.unirio.dsw.jpa.exemplo.model.manytoone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Correntista
{
	private Long id;
	private String nome;
	
	public Correntista()
	{
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
}