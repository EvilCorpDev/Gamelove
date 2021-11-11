package org.duckdns.androidghost77.gamelove.mapper;

import org.duckdns.androidghost77.gamelove.dto.GameRequest;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.repository.model.Game;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {
    Game gameRequestToGame(GameRequest gameRequest);
    GameResponse gameToGameResponse(Game game);
}
