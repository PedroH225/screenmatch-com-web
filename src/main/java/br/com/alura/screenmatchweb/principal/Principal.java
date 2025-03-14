package br.com.alura.screenmatchweb.principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatchweb.model.Categoria;
import br.com.alura.screenmatchweb.model.DadosSerie;
import br.com.alura.screenmatchweb.model.DadosTemporada;
import br.com.alura.screenmatchweb.model.Episodio;
import br.com.alura.screenmatchweb.model.Serie;
import br.com.alura.screenmatchweb.repository.SerieRepository;
import br.com.alura.screenmatchweb.service.ConsumoApi;
import br.com.alura.screenmatchweb.service.ConverteDados;

public class Principal {

	private Scanner leitura = new Scanner(System.in);
	private ConsumoApi consumo = new ConsumoApi();
	private ConverteDados conversor = new ConverteDados();
	private final String ENDERECO = "https://www.omdbapi.com/?t=";
	private final String API_KEY = "&apikey=6585022c";
	private List<Serie> series = new ArrayList<Serie>();
	private SerieRepository repositorio;

	public Principal(SerieRepository repositorio) {
		this.repositorio = repositorio;
	}

	public void exibeMenu() {
		var opcao = -1;
		while (opcao != 0) {

			var menu = """
					1 - Buscar séries
					2 - Buscar episódios
					3 - Listar séries buscadas
					4 - Buscar por ator
					5 - Top 5 Séries
					6 - Buscar por categoria
					7 - Buscar por temporada e avaliação
					8 - Buscar episódio por trecho
					9 - Top 5 episódios por série
					10 - Buscar por data de lançamento

					0 - Sair
					""";

			System.out.println(menu);
			opcao = leitura.nextInt();
			leitura.nextLine();

			System.out.println();
			switch (opcao) {
			case 1:
				buscarSerieWeb();
				break;
			case 2:
				buscarEpisodioPorSerie();
				break;
			case 3:
				listarSeriesBuscadas();
				break;
			case 4:
				buscarSériePorAtor();
				break;
			case 5:
				listarTop5Series();
				break;

			case 6:
				buscarPorCategoria();
				break;
			case 7:
				buscarPorTempAvaliacao();
				break;
			case 8:
				buscarEpisodioPorTrecho();
				break;
			case 9:
				top5EpsPorSerie();
				break;
			case 10:
				buscarPorLancamento();
				break;

			case 0:
				System.out.println("Saindo...");
				break;
			default:
				System.out.println("Opção inválida");
			}
		}
	}

	private void buscarPorLancamento() {
		Optional<Serie> buscarSerie = buscarPorTitulo();
		System.out.println("Digite uma data (dd/MM/yyyy):");
		String dataString = leitura.nextLine();

		System.out.println("Digite outra data (dd/MM/yyyy):");
		String dataString2 = leitura.nextLine();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		try {
			LocalDate data = LocalDate.parse(dataString, dtf);
			LocalDate data2 = LocalDate.parse(dataString2, dtf);

			if (buscarSerie.isPresent()) {
				List<Episodio> buscarEpisodios = repositorio.episodiosPorLancamento(buscarSerie.get(), data, data2);

				if (!buscarEpisodios.isEmpty()) {
					System.out.println(
							"Episódios com data de lançamento entre " + dataString + " e " + dataString2 + ":");

					buscarEpisodios.forEach(System.out::println);
				} else {
					System.out.println("Nenhum episódio encontrado.");
				}
			}
		} catch (DateTimeParseException e) {
			System.out.println("Data inválida: " + dataString);
		}
	}

	public void top5EpsPorSerie() {
		Optional<Serie> buscarSerie = buscarPorTitulo();

		if (buscarSerie.isPresent()) {
			System.out.println();
			System.out.println("Top 5 episódios da série " + buscarSerie.get().getTitulo() + ":");
			var top5Eps = repositorio.top5EpsPorSerie(buscarSerie.get());
			top5Eps.forEach(System.out::println);
		}
	}

	private Optional<Serie> buscarPorTitulo() {
		System.out.println("Digite o nome da série:");
		String nomeSerie = leitura.nextLine();

		var buscarSerie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

		if (buscarSerie.isPresent()) {
			System.out.println("Série encontrada!");
			System.out.println(buscarSerie.get());
		} else {
			System.out.println("Nenhuma série encontrada.");
		}

		return buscarSerie;
	}

	public void buscarEpisodioPorTrecho() {
		System.out.println("Digite o trecho do episódio:");
		String trecho = leitura.nextLine();

		var buscaEpisodios = repositorio.buscarEpisodioPorTrecho(trecho);

		if (!buscaEpisodios.isEmpty()) {
			buscaEpisodios.forEach(System.out::println);
		} else {
			System.out.println("Nenhum episódio encontrado!");
		}

	}

	private void buscarPorTempAvaliacao() {
		System.out.println("Digite o número máximo de temporadas:");
		Integer numTemp = leitura.nextInt();

		System.out.println("Digite a nota mínima de avaliação:");
		Double minAv = leitura.nextDouble();

		List<Serie> seriesBuscadas = repositorio.buscarTotalTemporadasAvaliacao(numTemp, minAv);

		System.out.println();
		if (!seriesBuscadas.isEmpty()) {
			seriesBuscadas.forEach(s -> {
				System.out.println(s.getTitulo() + ", Total de temporadas: " + s.getTotalTemporadas() + ", avaliação: "
						+ s.getAvaliacao());
			});
			System.out.println();
		} else {
			System.out.println("Nenhuma série encontrada!");
		}

	}

	private void buscarPorCategoria() {
		System.out.println("Digite a categoria: ");
		String categoriaBuscada = leitura.nextLine();

		Categoria categoria = Categoria.fromPortugues(categoriaBuscada);

		var seriesBuscadas = repositorio.findAllByGenero(categoria);

		System.out.println();
		if (!seriesBuscadas.isEmpty()) {
			seriesBuscadas.forEach(s -> {
				System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao());
			});
			System.out.println();
		} else {
			System.out.println("Nenhuma série encontrada!");
		}
	}

	private void buscarSerieWeb() {
		DadosSerie dados = getDadosSerie();
		repositorio.save(new Serie(dados));
		System.out.println(dados);
	}

	private DadosSerie getDadosSerie() {
		System.out.println("Digite o nome da série para busca");
		var nomeSerie = leitura.nextLine();
		System.out.println();
		var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		return dados;
	}

	private void buscarEpisodioPorSerie() {
		listarSeriesBuscadas();
		System.out.println("Digite uma série pelo nome: ");
		String tituloSerie = leitura.nextLine();

		Optional<Serie> buscarSerie = repositorio.findByTituloContainingIgnoreCase(tituloSerie);

		List<DadosTemporada> temporadas = new ArrayList<>();
		if (buscarSerie.isPresent()) {
			Serie serieBuscada = buscarSerie.get();
			for (int i = 1; i <= serieBuscada.getTotalTemporadas(); i++) {
				var json = consumo
						.obterDados(ENDERECO + serieBuscada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
				DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
				temporadas.add(dadosTemporada);
			}
			temporadas.forEach(System.out::println);

			List<Episodio> episodios = temporadas.stream()
					.flatMap(d -> d.episodios().stream().map(ep -> new Episodio(d.numero(), ep)))
					.collect(Collectors.toList());

			serieBuscada.setEpisodios(episodios);

			repositorio.save(serieBuscada);

		} else {
			System.out.println("Série não encontrada");
		}

	}

	private void listarSeriesBuscadas() {

		series = repositorio.findAll();

		series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);

	}

	private void buscarSériePorAtor() {
		System.out.println("Digite o nome do ator: ");
		String nomeAtor = leitura.nextLine();

		System.out.println("Avaliações a partir de que valor?: ");
		Double avaliacao = leitura.nextDouble();
		System.out.println();

		List<Serie> seriesBuscadas = repositorio
				.findAllByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);

		if (!seriesBuscadas.isEmpty()) {
			System.out.println("Séries em que " + nomeAtor + " trabalhou:");
			seriesBuscadas.forEach(s -> {
				System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao());
			});
			System.out.println();
		} else {
			System.out.println("Nenhuma série encontrada!");
		}

	}

	private void listarTop5Series() {
		System.out.println();

		List<Serie> top5Series = repositorio.findTop5ByOrderByAvaliacaoDesc();

		if (!top5Series.isEmpty()) {
			System.out.println("Top 5 séries:");
			top5Series.forEach(s -> {
				System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao());
			});
			System.out.println();
		} else {
			System.out.println("Nenhuma série encontrada");
		}
	}

}
