package br.com.alura.screenmatchweb.dto;

import br.com.alura.screenmatchweb.model.Categoria;

public record SerieDTO(String id, String titulo, Integer totalTemporadas, Double avaliacao, Categoria genero,
		String atores, String poster, String sinopse) {

}
