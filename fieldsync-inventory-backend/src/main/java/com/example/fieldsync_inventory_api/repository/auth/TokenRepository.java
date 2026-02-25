package com.example.fieldsync_inventory_api.repository.auth;

import java.util.List;
import java.util.Optional;
import com.example.fieldsync_inventory_api.entity.auth.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {

    @Query(value = """
            select t from TokenEntity t inner join t.user u\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<TokenEntity> findAllValidTokenByUser(Integer id);

    Optional<TokenEntity> findByToken(String token);
}
