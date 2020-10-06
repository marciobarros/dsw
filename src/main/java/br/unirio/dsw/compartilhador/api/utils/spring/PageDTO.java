package br.unirio.dsw.compartilhador.api.utils.spring;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe que representa uma página de objetos listados
 * 
 * @author User
 */
public class PageDTO<T>
{
	private List<T> itens;
	
	private @Getter @Setter long totalElements;
	
	private @Getter @Setter int totalPages;
	
	public PageDTO()
	{
		this.itens = new ArrayList<T>();
		this.totalElements = 0;
		this.totalPages = 0;
	}
	
	public void add(T item)
	{
		itens.add(item);
	}
	
	public Iterable<T> getItens()
	{
		return itens;
	}
}