package br.com.alura.screenmatchweb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.screenmatchweb.dto.SerieDTO;
import br.com.alura.screenmatchweb.service.SerieService;

@RestController
@RequestMapping("/series")
public class SerieControlador {

	private SerieService serieService;

	public SerieControlador(SerieService serieService) {
		this.serieService = serieService;
	}
	
	@GetMapping("/listar")
	public List<SerieDTO> listarSeries() {
		return serieService.listarSeries();
	}
	
	@GetMapping("/top5")
	public List<SerieDTO> listarTop5Series() {
		return serieService.listarTop5();
	}
	
}
