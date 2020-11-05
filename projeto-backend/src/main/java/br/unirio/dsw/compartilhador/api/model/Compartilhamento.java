package br.unirio.dsw.compartilhador.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 * Classe que representa o compartilhamento de um item
 * 
 * @author User
 */
@Entity
@Table(name="compartilhamento")
public class Compartilhamento implements Serializable
{
	private static final long serialVersionUID = 8943673290514855057L;
	
	private Long id;
	private Date dataRegistro;
	private Date dataAtualizacao;
	private Usuario usuario;
	private ItemCompartilhado item;
	private LocalDate dataInicio;
	private LocalDate dataTermino;
	private boolean aceito;
	private boolean rejeitado;
	private boolean canceladoDono;
	private boolean canceladoUsuario;

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
	
	@Column(name="data_registro", nullable=false)
	public Date getDataRegistro()
	{
		return dataRegistro;
	}
	
	public void setDataRegistro(Date dataRegistro)
	{
		this.dataRegistro = dataRegistro;
	}
	
	@Column(name="data_atualizacao", nullable=false)
	public Date getDataAtualizacao()
	{
		return dataAtualizacao;
	}
	
	public void setDataAtualizacao(Date dataAtualizacao)
	{
		this.dataAtualizacao = dataAtualizacao;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Usuario getUsuario() 
	{
		return usuario;
	}

	public void setUsuario(Usuario usuario) 
	{
		this.usuario = usuario;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	public ItemCompartilhado getItem() 
	{
		return item;
	}

	public void setItem(ItemCompartilhado item) 
	{
		this.item = item;
	}
	
	@Column(name="data_inicio", nullable=false)
	public LocalDate getDataInicio()
	{
		return dataInicio;
	}
	
	public void setDataInicio(LocalDate dataInicio)
	{
		this.dataInicio = dataInicio;
	}
	
	@Column(name="data_termino", nullable=false)
	public LocalDate getDataTermino()
	{
		return dataTermino;
	}
	
	public void setDataTermino(LocalDate dataTermino)
	{
		this.dataTermino = dataTermino;
	}
	
	@Column(name="aceito", nullable=false)
	public boolean isAceito()
	{
		return aceito;
	}
	
	public void setAceito(boolean aceito)
	{
		this.aceito = aceito;
	}
	
	@Column(name="rejeitado", nullable=false)
	public boolean isRejeitado()
	{
		return rejeitado;
	}
	
	public void setRejeitado(boolean rejeitado)
	{
		this.rejeitado = rejeitado;
	}

	@Column(name="cancelado_dono", nullable=false)
	public boolean isCanceladoDono()
	{
		return canceladoDono;
	}

	public void setCanceladoDono(boolean canceladoDono)
	{
		this.canceladoDono = canceladoDono;
	}

	@Column(name="cancelado_usuario", nullable=false)
	public boolean isCanceladoUsuario()
	{
		return canceladoUsuario;
	}

	public void setCanceladoUsuario(boolean canceladoUsuario)
	{
		this.canceladoUsuario = canceladoUsuario;
	}

	@PreUpdate
    public void preUpdate() 
	{
        dataAtualizacao = new Date();
    }
     
    @PrePersist
    public void prePersist() 
    {
        dataRegistro = dataAtualizacao = new Date();
    }
}