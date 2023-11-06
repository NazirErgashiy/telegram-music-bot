package uz.nazir.musicbot.repository.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.nazir.musicbot.repository.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.name =:name")
    User findByName(String name);

    @Query("SELECT u FROM User u WHERE u.code =:code")
    User findByCode(String code);
}
