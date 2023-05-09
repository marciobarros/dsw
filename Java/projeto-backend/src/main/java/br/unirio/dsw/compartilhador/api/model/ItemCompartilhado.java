package br.unirio.dsw.compartilhador.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 * Classe que representa um item compartilhado de um usuário
 * 
 * @author User
 */
@Entity
@Table(name="itemcompartilhado")
public class ItemCompartilhado implements Serializable
{
	private static final long serialVersionUID = 8943673290514855057L;
	
	private Long id;
	private Date dataRegistro;
	private Date dataAtualizacao;
	private Usuario usuario;
	private String nome;
	private String descricao;
	private TipoItemCompartilhado tipo;
	private boolean removido;
	private List<Compartilhamento> compartilhamentos;
	
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
	
	@Column(name="nome", nullable=false)
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	
	@Column(name="descricao", nullable=false)
	public String getDescricao()
	{
		return descricao;
	}
	
	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="tipo", nullable=false)
	public TipoItemCompartilhado getTipo()
	{
		return tipo;
	}

	public void setTipo(TipoItemCompartilhado tipo)
	{
		this.tipo = tipo;
	}

	@Column(name="removido", nullable=false)
	public boolean isRemovido()
	{
		return removido;
	}

	public void setRemovido(boolean removido)
	{
		this.removido = removido;
	}

	@OneToMany(mappedBy="item", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Compartilhamento> getCompartilhamentos() 
	{
		return compartilhamentos;
	}

	public void setCompartilhamentos(List<Compartilhamento> compartilhamentos) 
	{
		this.compartilhamentos = compartilhamentos;
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