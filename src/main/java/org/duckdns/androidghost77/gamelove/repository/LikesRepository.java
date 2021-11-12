package org.duckdns.androidghost77.gamelove.repository;

import org.duckdns.androidghost77.gamelove.repository.model.GameLikes;
import org.duckdns.androidghost77.gamelove.repository.model.Likes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, String> {

    @Query("SELECT l FROM Likes l WHERE l.game.id = :gameId AND l.user.id = :userId")
    Optional<Likes> findLikeByGameAndUser(
            @Param("gameId") String gameId,
            @Param("userId") String userId
    );

    @Query("SELECT l from Likes l WHERE l.user.id = :userId")
    List<Likes> getUserLikes(@Param("userId") String userId);

    @Query("SELECT new org.duckdns.androidghost77.gamelove.repository.model.GameLikes(l.game.id, COUNT(l)) "
            + "FROM Likes l GROUP BY l.game.id ORDER BY COUNT(l) DESC")
    Page<GameLikes> getMostLikedGames(Pageable pageable);

}
