package uz.nazir.musicbot.repository.sql;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.nazir.musicbot.repository.entity.UserEntity;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.name =:name")
    UserEntity findByName(String name);

    @Query("SELECT u FROM UserEntity u WHERE u.code =:code")
    UserEntity findByCode(String code);
}
