package br.com.alura.screenmatchweb.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.alura.screenmatchweb.dto.EpisodioDTO;
import br.com.alura.screenmatchweb.model.Categoria;
import br.com.alura.screenmatchweb.model.Episodio;
import br.com.alura.screenmatchweb.model.Serie;

@Repository
public interface SerieRepository extends JpaRepository<Serie, String> {

	Optional<Serie> findByTituloContainingIgnoreCase(String titulo);
	
	List<Serie> findAllByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String ator, Double avaliacao);
	
	List<Serie> findTop5ByOrderByAvaliacaoDesc();
	
	List<Serie> findAllByGenero(Categoria categoria);
	
	List<Serie> findAllByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer numTemp, Double avaliacao);
	
	@Query("select s from Serie s where s.totalTemporadas <= :numTemp AND s.avaliacao >= :avaliacao")
	List<Serie> buscarTotalTemporadasAvaliacao(Integer numTemp, Double avaliacao);
	
	@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trecho%")
	List<Episodio> buscarEpisodioPorTrecho(String trecho);

	@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE serie = :s ORDER BY e.avaliacao DESC LIMIT 5")
	List<Episodio> top5EpsPorSerie(Serie s);
	
	@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE serie = :s AND e.dataLancamento BETWEEN :data1 AND :data2")
	List<Episodio> episodiosPorLancamento(Serie s, LocalDate data1, LocalDate data2);
	
	@Query("SELECT s FROM Serie s " +
            "JOIN s.episodios e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> encontrarEpisodiosMaisRecentes();

	@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numTemp")
	List<Episodio> buscarPorIdTemporada(String id, Integer numTemp);
}


