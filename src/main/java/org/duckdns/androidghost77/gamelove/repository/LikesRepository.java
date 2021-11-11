package org.duckdns.androidghost77.gamelove.repository;

import org.duckdns.androidghost77.gamelove.repository.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Like, String> {

    @Query("SELECT l FROM Like l WHERE l.game.id = :gameId AND l.user.id = :userId")
    Optional<Like> findLikeByGameAndUser(
            @Param("gameId") String gameId,
            @Param("userId") String userId
    );

    @Query("SELECT l from Like l WHERE l.user.id = :userId")
    List<Like> getUserLikes(@Param("userId") String userId);

}
