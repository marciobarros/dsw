package br.unirio.dsw.jpa.exemplo.model.manytoone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ContaCorrente
{
	private Long id;
	private String banco;
	private String agencia;
	private String numero;
	private Correntista correntista;
	
	public ContaCorrente()
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

	public String getBanco()
	{
		return banco;
	}

	public void setBanco(String banco)
	{
		this.banco = banco;
	}

	public String getAgencia()
	{
		return agencia;
	}

	public void setAgencia(String agencia)
	{
		this.agencia = agencia;
	}

	public String getNumero()
	{
		return numero;
	}

	public void setNumero(String numero)
	{
		this.numero = numero;
	}

	@ManyToOne
	public Correntista getCorrentista()
	{
		return correntista;
	}

	public void setCorrentista(Correntista correntista)
	{
		this.correntista = correntista;
	}
}