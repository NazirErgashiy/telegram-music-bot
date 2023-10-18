package uz.nazir.musicbot.repository.sql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.nazir.musicbot.repository.entity.TrackEntity;

import java.util.List;

@Repository
public interface TracksRepository extends CrudRepository<TrackEntity, Long> {

    //JPQL
    @Query("SELECT t FROM TrackEntity t WHERE lower(t.name) LIKE lower(concat('%',:name,'%'))")
    List<TrackEntity> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT t FROM TrackEntity t WHERE lower(t.name) LIKE lower(concat('%',:name,'%'))")
    List<TrackEntity> findByName(@Param("name") String name);
}
