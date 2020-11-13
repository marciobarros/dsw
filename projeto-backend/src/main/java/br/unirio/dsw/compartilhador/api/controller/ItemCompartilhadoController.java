package br.unirio.dsw.compartilhador.api.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unirio.dsw.compartilhador.api.model.ItemCompartilhado;
import br.unirio.dsw.compartilhador.api.model.TipoItemCompartilhado;
import br.unirio.dsw.compartilhador.api.model.Usuario;
import br.unirio.dsw.compartilhador.api.repository.ItemCompartilhadoRepository;
import br.unirio.dsw.compartilhador.api.repository.UsuarioRepository;
import br.unirio.dsw.compartilhador.api.utils.spring.ControllerResponse;
import br.unirio.dsw.compartilhador.api.utils.spring.PageDTO;
import br.unirio.dsw.compartilhador.api.utils.spring.ResponseData;
import lombok.Data;

/**
 * Controlador com as ações de itens compartilhados
 * 
 * @author User
 */
@RestController
@RequestMapping("/api/item")
@CrossOrigin(origins = "*")
public class ItemCompartilhadoController
{
	private static final Logger log = LoggerFactory.getLogger(ItemCompartilhadoController.class);

	@Autowired
	private UsuarioRepository usuarioRepositorio;

	@Autowired
	private ItemCompartilhadoRepository itemRepositorio;
	
	/**
	 * Ação que lista os itens compartilhados de um usuário
	 */
	@GetMapping(value = "/lista")
	public ResponseEntity<ResponseData> list(@RequestParam int page, @RequestParam int per_page)
	{
		log.info("Listando items dono");
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (username == null)
			return ControllerResponse.fail("Não há um usuário logado no sistema.");

        Usuario usuario = usuarioRepositorio.findByEmail(username);

		if (usuario == null)
			return ControllerResponse.fail("Não foi possível recuperar os dados do usuário a partir das credenciais.");
		
		Pageable pageable = PageRequest.of(page-1, per_page);
		Page<ItemCompartilhado> itens = itemRepositorio.findByUsuarioId(usuario.getId(), pageable);

		PageDTO<ItemCompartilhadoDTO> result = new PageDTO<ItemCompartilhadoDTO>(itens.getTotalElements(), page, per_page);
		
		itens.forEach(item -> {
			ItemCompartilhadoDTO dto = new ItemCompartilhadoDTO();
			dto.setId(item.getId());
			dto.setNome(item.getNome());
			dto.setDescricao(item.getDescricao());
			dto.setTipo(item.getTipo().toString());
			result.add(dto);
		});
		
		return ControllerResponse.success(result);
	}
	
	/**
	 * Ação que cria um novo item compartilhado
	 */
	@PutMapping(value = "/novo")
	public ResponseEntity<ResponseData> novo(@RequestBody NovoItemCompartilhadoForm form, BindingResult result)
	{
		log.info("Criando um novo item compartilhado: {}", form.toString());
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (username == null)
			return ControllerResponse.fail("nome", "Não há um usuário logado no sistema.");

        Usuario usuario = usuarioRepositorio.findByEmail(username);

		if (usuario == null)
			return ControllerResponse.fail("nome", "Não foi possível recuperar os dados do usuário a partir das credenciais.");

		if (form.getNome().length() == 0)
			return ControllerResponse.fail("nome", "O nome do item compartilhado não pode ser vazio.");

		if (form.getNome().length() > 80)
			return ControllerResponse.fail("nome", "O nome do item compartilhado não pode ter mais do que 80 caracteres.");
		
		ItemCompartilhado itemAnterior = itemRepositorio.findByNomeAndUsuarioId(form.getNome(), usuario.getId());
		
		if (itemAnterior != null)
			return ControllerResponse.fail("nome", "Já existe um item compartilhado registrado com este nome no sistema.");

		if (form.getDescricao().length() == 0)
			return ControllerResponse.fail("descricao", "A descrição do item compartilhado não pode ser vazia.");

		if (form.getDescricao().length() > 255)
			return ControllerResponse.fail("descricao", "A descrição do item compartilhado não pode ter mais do que 255 caracteres.");

		if (form.getTipo().compareToIgnoreCase("unico") != 0 && form.getTipo().compareToIgnoreCase("multiplo") != 0)
			return ControllerResponse.fail("tipo", "O tipo do item compartilhado deve ser 'único' ou 'múltiplo'.");
		
		TipoItemCompartilhado tipo = (form.getTipo().compareToIgnoreCase("unico") == 0) ? TipoItemCompartilhado.UNICO : TipoItemCompartilhado.MULTIPLO;
 
        ItemCompartilhado item = new ItemCompartilhado();
        item.setNome(form.getNome());
        item.setDescricao(form.getDescricao());
        item.setTipo(tipo);
        item.setUsuario(usuario);
        itemRepositorio.save(item);
		return ControllerResponse.success();
	}
	
	/**
	 * Ação que atualiza os dados de um item compartilhado
	 */
	@PostMapping(value = "/atualiza")
	public ResponseEntity<ResponseData> atualiza(@RequestBody AtualizaItemCompartilhadoForm form, BindingResult result)
	{
		log.info("Atualizando um item compartilhado: {}", form.toString());
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (username == null)
			return ControllerResponse.fail("nome", "Não há um usuário logado no sistema.");

        Usuario usuario = usuarioRepositorio.findByEmail(username);

		if (usuario == null)
			return ControllerResponse.fail("nome", "Não foi possível recuperar os dados do usuário a partir das credenciais.");

		Optional<ItemCompartilhado> item = itemRepositorio.findById(form.getId());
		
		if (!item.isPresent())
			return ControllerResponse.fail("nome", "O item compartilhado não foi encontrado.");
		
		if (item.get().getUsuario().getId() != usuario.getId())
			return ControllerResponse.fail("nome", "O item compartilhado não pertence ao usuário logado.");

		if (item.get().isRemovido())
			return ControllerResponse.fail("nome", "O item compartilhado já foi removido.");

		if (form.getNome().length() == 0)
			return ControllerResponse.fail("nome", "O nome do item compartilhado não pode ser vazio.");

		if (form.getNome().length() > 80)
			return ControllerResponse.fail("nome", "O nome do item compartilhado não pode ter mais do que 80 caracteres.");
		
		ItemCompartilhado itemAnterior = itemRepositorio.findByNomeAndUsuarioId(form.getNome(), usuario.getId());
		
		if (itemAnterior != null && itemAnterior.getId() != form.getId())
			return ControllerResponse.fail("nome", "Já existe um item compartilhado registrado com este nome no sistema.");

		if (form.getDescricao().length() == 0)
			return ControllerResponse.fail("descricao", "A descrição do item compartilhado não pode ser vazia.");

		if (form.getDescricao().length() > 255)
			return ControllerResponse.fail("descricao", "A descrição do item compartilhado não pode ter mais do que 255 caracteres.");

		if (form.getTipo().compareToIgnoreCase("unico") != 0 && form.getTipo().compareToIgnoreCase("multiplo") != 0)
			return ControllerResponse.fail("tipo", "O tipo do item compartilhado deve ser 'único' ou 'múltiplo'.");
		
		TipoItemCompartilhado tipo = (form.getTipo().compareToIgnoreCase("unico") == 0) ? TipoItemCompartilhado.UNICO : TipoItemCompartilhado.MULTIPLO;
 
        item.get().setNome(form.getNome());
        item.get().setDescricao(form.getDescricao());
        item.get().setTipo(tipo);
        itemRepositorio.save(item.get());
		return ControllerResponse.success();
	}
	
	/**
	 * Ação que remove um item compartilhado
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ResponseData> remove(@PathVariable("id") long id)
	{
		log.info("Removendo um item compartilhado: {}", id);
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (username == null)
			return ControllerResponse.fail("Não há um usuário logado no sistema.");

        Usuario usuario = usuarioRepositorio.findByEmail(username);

		if (usuario == null)
			return ControllerResponse.fail("Não foi possível recuperar os dados do usuário a partir das credenciais.");

		Optional<ItemCompartilhado> item = itemRepositorio.findById(id);
		
		if (!item.isPresent())
			return ControllerResponse.fail("O item compartilhado não foi encontrado.");
		
		if (item.get().getUsuario().getId() != usuario.getId())
			return ControllerResponse.fail("O item compartilhado não pertence ao usuário logado.");

		if (item.get().isRemovido())
			return ControllerResponse.fail("O item compartilhado já foi removido.");

        item.get().setRemovido(true);
        itemRepositorio.save(item.get());
		return ControllerResponse.success();
	}
}

/**
 * Formulário para registrar um novo item compartilhado
 * 
 * @author User
 */
@Data class NovoItemCompartilhadoForm
{
	private String nome;
	
	private String descricao;
	
	private String tipo;
}

/**
 * Formulário para atualizar um item compartilhado
 * 
 * @author User
 */
@Data class AtualizaItemCompartilhadoForm
{
	private long id;
	
	private String nome;
	
	private String descricao;
	
	private String tipo;
}

/**
 * Classe que representa um item compartilhado para o lado cliente 
 * 
 * @author User
 */
@Data class ItemCompartilhadoDTO
{
	private long id;
	
	private String nome;
	
	private String descricao;
	
	private String tipo;
}