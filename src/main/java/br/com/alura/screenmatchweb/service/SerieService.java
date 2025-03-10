package br.com.alura.screenmatchweb.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import br.com.alura.screenmatchweb.dto.EpisodioDTO;
import br.com.alura.screenmatchweb.dto.SerieDTO;
import br.com.alura.screenmatchweb.model.Episodio;
import br.com.alura.screenmatchweb.model.Serie;
import br.com.alura.screenmatchweb.repository.SerieRepository;

@Service
public class SerieService {

	private SerieRepository serieRepository;

	public SerieService(SerieRepository serieRepository) {
		this.serieRepository = serieRepository;
	}
	
	public List<SerieDTO> listarSeries() {
		return converterSerieDTO(serieRepository.findAll());
	}

	public List<SerieDTO> listarTop5() {
		return converterSerieDTO(serieRepository.findTop5ByOrderByAvaliacaoDesc());
	}
	
	public List<SerieDTO> obterPorLancamento() {
		return converterSerieDTO(serieRepository.encontrarEpisodiosMaisRecentes());
	}
	
	private List<SerieDTO> converterSerieDTO(List<Serie> series) {
		return series.stream()
				.map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
						s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
				.collect(Collectors.toList());
	}
	
	private SerieDTO converterSerieDTO(Serie s) {
		return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
				s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse());
	}
	
	private List<EpisodioDTO> converterEpisodioDTO(Serie s) {
		return s.getEpisodios().stream()
				.map(ep -> new EpisodioDTO(ep.getId(), ep.getTitulo(), ep.getTemporada(), ep.getNumeroEpisodio()))
				.collect(Collectors.toList());
	}
	
	private List<EpisodioDTO> converterEpisodioDTO(List<Episodio> eps) {
		return eps.stream()
				.map(ep -> new EpisodioDTO(ep.getId(), ep.getTitulo(), ep.getTemporada(), ep.getNumeroEpisodio()))
				.collect(Collectors.toList());
	}


	public SerieDTO obterPorId(String id) {
		Optional<Serie> buscarSerie = serieRepository.findById(id);
		
		if (buscarSerie.isPresent()) {
			return converterSerieDTO(buscarSerie.get());
		}
		return null;
	}

	public List<EpisodioDTO> obterEpisodios(String id) {
		Optional<Serie> buscarSerie = this.serieRepository.findById(id);
		if (buscarSerie.isPresent()) {
			return converterEpisodioDTO(buscarSerie.get());
		}
		return null;
	}

	public List<EpisodioDTO> obterEpisodiosTemporada(String id, Integer numTemp) {
		List<Episodio> buscarEpisodios = this.serieRepository.buscarPorIdTemporada(id, numTemp);
		if (!buscarEpisodios.isEmpty()) {
			return converterEpisodioDTO(buscarEpisodios);
		}
		return null;
	}
}




