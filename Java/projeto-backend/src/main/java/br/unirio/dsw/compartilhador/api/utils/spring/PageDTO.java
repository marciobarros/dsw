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
	private List<T> data;
	
	private @Getter @Setter long total;

	private @Getter @Setter int current_page;
	
	private @Getter @Setter int per_page;	
	
	public PageDTO(long total, int currentPage, int pageSize)
	{
		this.data = new ArrayList<T>();
		this.total = total;
		this.current_page = currentPage;
		this.per_page = pageSize;
	}
	
	public int getLast_page()
	{
		int last = (int)(total / per_page) + ((total % per_page == 0) ? 0 : 1);
		return Math.max(last,  1);
	}
	
	public int getFrom()
	{
		return (current_page - 1) * per_page + 1;
	}
	
	public int getTo()
	{
		return current_page * per_page;
	}
	
	public void add(T item)
	{
		data.add(item);
	}
	
	public Iterable<T> getData()
	{
		return data;
	}
}