package com.greendog.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greendog.domain.Cliente;
import com.greendog.domain.Item;
import com.greendog.domain.Pedido;
import com.greendog.repository.ClienteRepository;
import com.greendog.repository.ItemRepository;
import com.greendog.repository.PedidoRepository;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

	private final PedidoRepository pedidoRepository;
	private final ItemRepository itemRepository;
	private final ClienteRepository clienteRepository;
	private final String PEDIDO_URI = "pedidos/";
	
	public PedidoController(PedidoRepository pedidoRepository, ItemRepository itemRepository,
			ClienteRepository clienteRepository) {
		this.pedidoRepository = pedidoRepository;
		this.itemRepository = itemRepository;
		this.clienteRepository = clienteRepository;
	}
	
	@GetMapping("/")
	public ModelAndView list(){
		Iterable<Pedido> pedidos = pedidoRepository.findAll();
		return new ModelAndView(PEDIDO_URI+"list", "pedidos", pedidos);
	}
	
	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Pedido pedido){
		return new ModelAndView(PEDIDO_URI+"view", "pedido", pedido);
	}
	
	@GetMapping("/novo")
	public ModelAndView createForm(@ModelAttribute Pedido pedido){
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("todosItens", itemRepository.findAll());
		model.put("todosCliente", clienteRepository.findAll());
		return new ModelAndView(PEDIDO_URI+"form", "form", model);
	}
	
	@PostMapping(params="form")
	public ModelAndView create(@Valid Pedido pedido, BindingResult result, RedirectAttributes attributes){
		if (result.hasErrors()) {
			return new ModelAndView(PEDIDO_URI+"form", "formErrors", result.getAllErrors());
		}
		
		if (pedido.getId()!=null) {
			Pedido pedidoParaAlterar = pedidoRepository.findOne(pedido.getId());
			Cliente cliente = clienteRepository.findOne(pedidoParaAlterar.getCliente().getId());
			pedidoParaAlterar.setItens(pedido.getItens());
			double valorTotal = 0;
			for(Item item : pedido.getItens()){
				valorTotal += item.getPreco();
			}
			pedidoParaAlterar.setData(pedido.getData());
			pedidoParaAlterar.setValorTotal(valorTotal);;
			cliente.getPedidos().remove(pedidoParaAlterar.getId());
			cliente.getPedidos().add(pedidoParaAlterar);
			this.clienteRepository.save(cliente);
		} else {
			Cliente cliente = clienteRepository.findOne(pedido.getCliente().getId());
			double valorTotal = 0;
			for (Item item : pedido.getItens()) {
				valorTotal +=item.getPreco();
			}
			pedido.setValorTotal(valorTotal);
			pedido = this.pedidoRepository.save(pedido);
			cliente.getPedidos().add(pedido);
			this.clienteRepository.save(cliente);
			attributes.addFlashAttribute("globalMessage", "Pedido salvo com sucesso!");
		}
		return new ModelAndView(PEDIDO_URI+"{pedido.id}", "pedido.id", pedido.getId());
	}
	
	@GetMapping(value = "remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id,RedirectAttributes redirect) {

		Pedido pedidoParaRemover = pedidoRepository.findOne(id);

		Cliente c = clienteRepository.findOne(pedidoParaRemover.getCliente().getId());
		c.getPedidos().remove(pedidoParaRemover);

		this.clienteRepository.save(c);
		this.pedidoRepository.delete(id);

		Iterable<Pedido> pedidos = this.pedidoRepository.findAll();

		ModelAndView mv = new ModelAndView(PEDIDO_URI+ "list","pedidos",pedidos);
		mv.addObject("globalMessage","Pedido removido com sucesso");

		return mv;
	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Pedido pedido) {

		Map<String,Object> model = new HashMap<String,Object>();
		model.put("todosItens",itemRepository.findAll());
		model.put("todosClientes",clienteRepository.findAll());
		model.put("pedido",pedido);

		return new ModelAndView(PEDIDO_URI + "form",model);
	}
	
}
