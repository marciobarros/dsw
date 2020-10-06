package br.unirio.dsw.jpa.exemplo.model.manytomany;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Usuario
{
	private Long id;
	private String nome;
	private Collection<Grupo> grupos;
	
	public Usuario()
	{
		this.grupos = new ArrayList<Grupo>();
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

	@ManyToMany
	public Collection<Grupo> getGrupos()
	{
		return grupos;
	}

	public void setGrupos(Collection<Grupo> grupos)
	{
		this.grupos = grupos;
	}
}