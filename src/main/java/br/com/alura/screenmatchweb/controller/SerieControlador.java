package br.com.alura.screenmatchweb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.screenmatchweb.dto.SerieDTO;
import br.com.alura.screenmatchweb.model.Serie;
import br.com.alura.screenmatchweb.repository.SerieRepository;

@RestController
@RequestMapping("/series")
public class SerieControlador {

	private SerieRepository repository;

	public SerieControlador(SerieRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/listar")
	public List<SerieDTO> listarSeries() {
		return repository.findAll().stream()
				.map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
						s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
				.collect(Collectors.toList());
	}
	
}
