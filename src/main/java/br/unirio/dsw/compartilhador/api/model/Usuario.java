package br.unirio.dsw.compartilhador.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Classe que representa um usuário no sistema
 * 
 * @author User
 */
@Entity
@Table(name="usuario")
public class Usuario implements Serializable
{
	private static final long serialVersionUID = 7111897663449750505L;

	private Long id;
	private Date dataRegistro;
	private Date dataAtualizacao;
	private String nome;
	private String email;
	private String senha;
	private String tokenSenha;
	private Date dataTokenSenha;
	private int falhasLogin;
	private boolean bloqueado;
	private boolean administrador;
	private List<ItemCompartilhado> items;
	
	public Usuario()
	{
		this.falhasLogin = 0;
		this.bloqueado = false;
		this.administrador = false;
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
	
	@Column(name="nome", nullable=false)
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	
	@Column(name="email", nullable=false)
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	@Column(name="senha", nullable=false)
	public String getSenha()
	{
		return senha;
	}
	
	public void setSenha(String senha)
	{
		this.senha = senha;
	}
	
	@Column(name="token_senha", nullable=true)
	public String getTokenSenha()
	{
		return tokenSenha;
	}
	
	public void setTokenSenha(String tokenSenha)
	{
		this.tokenSenha = tokenSenha;
	}
	
	@Column(name="data_token_senha", nullable=true)
	public Date getDataTokenSenha()
	{
		return dataTokenSenha;
	}
	
	public void setDataTokenSenha(Date dataTokenSenha)
	{
		this.dataTokenSenha = dataTokenSenha;
	}
	
	@Column(name="falhas_login", nullable=false)
	public int getFalhasLogin()
	{
		return falhasLogin;
	}
	
	public void setFalhasLogin(int falhasLogin)
	{
		this.falhasLogin = falhasLogin;
	}
	
	@Column(name="bloqueado", nullable=false)
	public boolean isBloqueado()
	{
		return bloqueado;
	}
	
	public void setBloqueado(boolean bloqueado)
	{
		this.bloqueado = bloqueado;
	}
	
	@Column(name="administrador", nullable=false)
	public boolean isAdministrador()
	{
		return administrador;
	}

	public void setAdministrador(boolean administrador)
	{
		this.administrador = administrador;
	}

	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<ItemCompartilhado> getItems()
	{
		return items;
	}

	public void setItems(List<ItemCompartilhado> items)
	{
		this.items = items;
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

    @Transient
	public List<GrantedAuthority> getAutorizacoes()
	{
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		if (administrador)
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		else
			authorities.add(new SimpleGrantedAuthority("ROLE_BASIC"));
		
		return authorities;
	}
}