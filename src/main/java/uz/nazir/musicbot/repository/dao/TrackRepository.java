package uz.nazir.musicbot.repository.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.nazir.musicbot.repository.entity.Track;

import java.util.List;

@Repository
public interface TrackRepository extends CrudRepository<Track, Long> {

    //JPQL
    @Query("SELECT t FROM Track t WHERE lower(t.name) LIKE lower(concat('%',:name,'%'))")
    List<Track> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT t FROM Track t WHERE lower(t.name) LIKE lower(concat('%',:name,'%'))")
    List<Track> findByName(@Param("name") String name);
}
