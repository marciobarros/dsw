package br.unirio.dsw.jpa.exemplo.model.onetoone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Endereco
{
	private Long id;
	private String localidade;
	private String numero;
	private String complemento;
	private String municipio;
	private String estado;
	private String cep;

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

	public String getLocalidade()
	{
		return localidade;
	}

	public void setLocalidade(String localidade)
	{
		this.localidade = localidade;
	}

	public String getNumero()
	{
		return numero;
	}

	public void setNumero(String numero)
	{
		this.numero = numero;
	}

	public String getComplemento()
	{
		return complemento;
	}

	public void setComplemento(String complemento)
	{
		this.complemento = complemento;
	}

	public String getMunicipio()
	{
		return municipio;
	}

	public void setMunicipio(String municipio)
	{
		this.municipio = municipio;
	}

	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	public String getCep()
	{
		return cep;
	}

	public void setCep(String cep)
	{
		this.cep = cep;
	}
}