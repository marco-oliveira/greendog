package com.greendog.carga;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.greendog.domain.Cliente;
import com.greendog.domain.Item;
import com.greendog.domain.Pedido;
import com.greendog.repository.ClienteRepository;

@Component
public class RepositoryTest implements ApplicationRunner {
	
	private	static final long ID_CLIENTE_MARCO = 11l;
	private	static final long ID_CLIENTE_ZE_PEQUENO	= 22l;
	
	private	static final long ID_ITEM1 = 100l;
	private	static final long ID_ITEM2 = 101l;
	private	static final long ID_ITEM3 = 102l;
	
	private	static final long ID_PEDIDO1 = 1000l;
	private	static final long ID_PEDIDO2 = 1001l;
	private	static final long ID_PEDIDO3 = 1002l;
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {

		System.out.println(">>>>>>>>Iniciando Carga de Dados!");
		Cliente marco = new Cliente(ID_CLIENTE_MARCO,"Marco", "Anápolis");
		Cliente zePequeno = new Cliente(ID_CLIENTE_ZE_PEQUENO,"Ze Pequeno", "Cidade de Deus");
		
		Item dog1 = new Item(ID_ITEM1, "Green Dog Tradicional", 21d);
		Item dog2 = new Item(ID_ITEM2, "Green Dog Apimentado", 25d);
		Item dog3 = new Item(ID_ITEM3, "Green Dog Com Queijo", 27d);
		
		List<Item> listaPedidosMarco = new ArrayList<>();
		listaPedidosMarco.add(dog1);
		
		List<Item> listaPedidosZePequeno = new ArrayList<>();
		listaPedidosZePequeno.add(dog2);
		listaPedidosZePequeno.add(dog3);
		
		Pedido pedidoDoMarco = new Pedido(ID_PEDIDO1, marco, listaPedidosMarco, dog1.getPreco());
		marco.novoPedido(pedidoDoMarco);
		
		
		Pedido pedidoDoZePequeno = new Pedido(ID_PEDIDO2, zePequeno, listaPedidosZePequeno, dog2.getPreco()+dog3.getPreco());
		zePequeno.novoPedido(pedidoDoZePequeno);
		
		System.out.println("Pedido 1 - Marco "+ pedidoDoMarco);
		System.out.println("Pedido 2 - Ze Pequeno "+ pedidoDoZePequeno);
		
		clienteRepository.saveAndFlush(zePequeno);
		System.out.println("<<<<<Gravando Cliente 2 "+zePequeno);
		List<Item> listaPedidosMarco2 = new ArrayList<>();
		listaPedidosMarco.add(dog2);
		
		Pedido pedido2DoMarco = new Pedido(ID_PEDIDO3, marco, listaPedidosMarco2, dog2.getPreco());
		marco.novoPedido(pedido2DoMarco);
		
	
		clienteRepository.saveAndFlush(marco);
		System.out.println("Pedido 2 - Marco "+ pedido2DoMarco);
		System.out.println("<<<<<Gravando Cliente 1 "+marco);
		
		
	}
}
