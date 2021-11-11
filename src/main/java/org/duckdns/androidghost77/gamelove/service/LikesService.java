package org.duckdns.androidghost77.gamelove.service;

import org.duckdns.androidghost77.gamelove.dto.GameResponse;

import java.util.List;

public interface LikesService {

    void likeGame(String gameId, String userId);

    void unlikeGame(String gameId, String userId);

    List<GameResponse> getGamesLikedByUser(String userId);
}
