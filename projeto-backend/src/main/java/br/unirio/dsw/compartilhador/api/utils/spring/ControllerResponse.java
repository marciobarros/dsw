package br.unirio.dsw.compartilhador.api.utils.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * Classe utilitária com métodos padronizados de retorno dos controladores
 * 
 * @author User
 */
public class ControllerResponse
{
	/**
	 * Indica que uma ação foi concluída com sucesso e sem resposta em um controlador
	 */
	public static ResponseEntity<ResponseData> success()
	{
		ResponseData responseData = new ResponseData();
		return ResponseEntity.ok().body(responseData);
	}

	/**
	 * Indica que uma ação foi concluída com sucesso e com resposta em um controlador
	 */
	public static ResponseEntity<ResponseData> success(Object data)
	{
		ResponseData responseData = new ResponseData();
		responseData.setData(data);
		return ResponseEntity.ok().body(responseData);
	}

	/**
	 * Indica que uma ação foi concluída com erro em um controlador
	 */
	public static ResponseEntity<ResponseData> fail(String message)
	{
		ResponseData responseData = new ResponseData();
		responseData.addError(message);		
		return ResponseEntity.badRequest().body(responseData);
	}

	/**
	 * Indica que uma ação foi concluída com erro em um controlador
	 */
	public static ResponseEntity<ResponseData> fail(String field, String message)
	{
		ResponseData responseData = new ResponseData();
		responseData.addError(field, message);		
		return ResponseEntity.badRequest().body(responseData);
	}

	/**
	 * Indica que uma ação foi concluída com um conjunto de erros em um controlador
	 */
	public static ResponseEntity<ResponseData> fail(BindingResult result)
	{
		ResponseData responseData = new ResponseData();
		
		for (ObjectError error : result.getAllErrors())
			responseData.addError(error.getDefaultMessage());
		
		return ResponseEntity.badRequest().body(responseData);
	}
}