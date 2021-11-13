package org.duckdns.androidghost77.gamelove.service;

import org.duckdns.androidghost77.gamelove.dto.GameRequest;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;

import java.util.List;

public interface GameService {

    GameResponse addGame(GameRequest gameRequest);

    GameResponse findGameById(String id);

    List<GameResponse> findGamesByName(String name);
}
