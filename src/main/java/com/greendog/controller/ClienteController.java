package com.greendog.controller;


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
import com.greendog.repository.ClienteRepository;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	private final ClienteRepository clienteRepository;
	private final String CLIENTE_URI = "clientes/";
	
	public ClienteController(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}
	
	@GetMapping("/")
	public ModelAndView list(){
		Iterable<Cliente> clientes = this.clienteRepository.findAll();
		return new ModelAndView(CLIENTE_URI+"list", "clientes", clientes);
	}
	
	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Cliente cliente){
		return new ModelAndView(CLIENTE_URI+"view", "cliente", cliente );
	}
	
	@GetMapping("/novo")
	public String createForm(@ModelAttribute Cliente cliente){
		return CLIENTE_URI+"form";
	}
	
	@PostMapping(params="form")
	public ModelAndView create(@Valid Cliente cliente, BindingResult bindingResult, RedirectAttributes attributes){
		if (bindingResult.hasErrors()) {
			return new ModelAndView(CLIENTE_URI+"form", "formErrors", bindingResult.getAllErrors());
		}
		
		cliente = this.clienteRepository.save(cliente);
		attributes.addFlashAttribute("globalMessage","Cliente salvo com sucesso!");
		return new ModelAndView("redirect:/" + CLIENTE_URI + "{cliente.id}","cliente.id",cliente.getId());
	}
	
	@GetMapping(value="remover/{id}")
	public ModelAndView remover(@PathVariable Long id, RedirectAttributes attributes){
		this.clienteRepository.delete(id);
		Iterable<Cliente> clientes = this.clienteRepository.findAll();
		
		ModelAndView mv = new ModelAndView(CLIENTE_URI+"list", "clientes", clientes);
		mv.addObject("globalMessage", "Cliente excluido com sucesso!");
		return mv;
	}
	
	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Cliente cliente) {
		return new ModelAndView(CLIENTE_URI + "form","cliente",cliente);
	}

}
