package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Integer> {
     // Exemplo de método customizado: Buscar fotógrafos por nome
     static List<Photographer> findByNomeContainingIgnoreCase(String nome) {
        return null;
    }

    // Exemplo de consulta JPQL: Buscar fotógrafos por país
    @Query("SELECT f FROM Photographer f WHERE f.pais = :pais")
    static List<Photographer> findByPais(@Param("pais") String pais) {
        return null;
    }

    // Exemplo de consulta nativa: Buscar fotógrafos por cidade
    @Query(value = "SELECT * FROM Photographer WHERE cidade = :cidade", nativeQuery = true)
    static List<Photographer> findByCidade(@Param("cidade") String cidade) {
        return null;
    }

}