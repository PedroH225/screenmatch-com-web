package br.com.alura.screenmatchweb;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.screenmatchweb.repository.SerieRepository;

@RestController
@RequestMapping("/series")
public class SerieControlador {

	private SerieRepository repository;

	public SerieControlador(SerieRepository repository) {
		this.repository = repository;
	}
	
	
}
