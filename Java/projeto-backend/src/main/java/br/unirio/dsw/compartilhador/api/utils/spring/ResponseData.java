package br.unirio.dsw.compartilhador.api.utils.spring;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe que representa uma resposta a uma requisição
 * 
 * @author User
 */
public class ResponseData
{
	private @Getter @Setter Object data;
	private HashMap<String, String> errors;

	public ResponseData()
	{
		this.errors = new HashMap<String, String>();
	}
	
	public String getTimestamp()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date()).replace(" ", "T") + ".000+00:00";
	}

	public int getStatus()
	{
		return hasErrors() ? HttpServletResponse.SC_BAD_REQUEST : HttpServletResponse.SC_OK;
	}

	public void addError(String message)
	{
		this.errors.put("all", message);
	}
	
	public void addError(String field, String message)
	{
		this.errors.put(field, message);
	}
	
	public HashMap<String, String> getErrors()
	{
		return errors;
	}

	public boolean hasErrors()
	{
		return errors.size() > 0;
	}
}