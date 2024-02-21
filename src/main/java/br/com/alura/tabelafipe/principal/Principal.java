package br.com.alura.tabelafipe.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.tabelafipe.model.Dados;
import br.com.alura.tabelafipe.model.Modelos;
import br.com.alura.tabelafipe.model.Veiculo;
import br.com.alura.tabelafipe.service.ConsumoApi;
import br.com.alura.tabelafipe.service.ConverteDados;

public class Principal {
	
	ConsumoApi consumo = new ConsumoApi();
	
	private final Scanner leitor = new Scanner(System.in);
	
	private ConverteDados conversor = new ConverteDados();
	
	private String endereco = "https://parallelum.com.br/fipe/api/v1/";

	public void exibirMenu() {
		System.out.println("Olá bem vindo a consulta da tabela fipe");
		System.out.println("***************************************");
		System.out.println("informe o tipo desejado para buscar as marcas");
		System.out.println("carros, motos ou caminhores");
		var tipo = leitor.nextLine();

		endereco = endereco + tipo + "/marcas";

		String json = consumo.obterDados(endereco);
		var marcas = conversor.obterLista(json, Dados.class);

		marcas.stream()
			.sorted(Comparator.comparing(Dados::nome))
			.forEach(System.out::println);

		System.out.print("Informe o codigo da marque deseja selecionar: ");
		var codigoMarca = leitor.nextLine();
		
		endereco = endereco + "/" + codigoMarca + "/modelos";
		json = consumo.obterDados(endereco);
		var modeloLista = conversor.obterDados(json, Modelos.class);

		System.out.println("\n modelos da marca: ");
		modeloLista.modelos().stream()
			.sorted((Comparator.comparing(Dados::codigo)))
			.forEach(System.out::println);

		System.out.println("\n Digite um trecho do nome do carro a ser buscado:");
		var nomeVeiculo = leitor.nextLine();

		List<Dados> modelosFiltrados = modeloLista.modelos().stream()
				.filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
				.collect(Collectors.toList());
		
		
		System.out.println("\n Modelos filtrados");
		modelosFiltrados.forEach(System.out::println);
		
		
		System.out.println("Digite por favor o código do modelo para verificar as avaliações: ");
		var codigoModelo = leitor.nextLine();
		
		endereco = endereco + "/" + codigoModelo + "/anos";
		json = consumo.obterDados(endereco);
		List<Dados> anos = conversor.obterLista(json, Dados.class);
		List<Veiculo> veiculos = new ArrayList<>();
		
		
		for (int i = 0; i < anos.size(); i++) {
			var enderecoAnos = endereco + "/" + anos.get(i).codigo();
			json = consumo.obterDados(enderecoAnos);
			Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
			veiculos.add(veiculo);				
		}
		
		System.out.println("\n Todos os veículos filtrados com avaliações por ano: ");
		veiculos.forEach(System.out::println);
		
		}
}
