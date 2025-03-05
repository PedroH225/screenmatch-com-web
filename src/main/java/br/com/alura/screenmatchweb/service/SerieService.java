package br.com.alura.screenmatchweb.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.alura.screenmatchweb.dto.SerieDTO;
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

	public SerieDTO obterPorId(String id) {
		Optional<Serie> buscarSerie = serieRepository.findById(id);
		
		if (buscarSerie.isPresent()) {
			return converterSerieDTO(buscarSerie.get());
		}
		return null;
	}
}
